import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('JobHistory e2e test', () => {
  const jobHistoryPageUrl = '/job-history';
  const jobHistoryPageUrlPattern = new RegExp('/job-history(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const jobHistorySample = {};

  let jobHistory: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/job-histories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/job-histories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/job-histories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (jobHistory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/job-histories/${jobHistory.id}`,
      }).then(() => {
        jobHistory = undefined;
      });
    }
  });

  it('JobHistories menu should load JobHistories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('job-history');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('JobHistory').should('exist');
    cy.url().should('match', jobHistoryPageUrlPattern);
  });

  describe('JobHistory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(jobHistoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create JobHistory page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/job-history/new$'));
        cy.getEntityCreateUpdateHeading('JobHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', jobHistoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/job-histories',
          body: jobHistorySample,
        }).then(({ body }) => {
          jobHistory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/job-histories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [jobHistory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(jobHistoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details JobHistory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('jobHistory');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', jobHistoryPageUrlPattern);
      });

      it('edit button click should load edit JobHistory page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('JobHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', jobHistoryPageUrlPattern);
      });

      it('last delete button click should delete instance of JobHistory', () => {
        cy.intercept('GET', '/api/job-histories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('jobHistory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', jobHistoryPageUrlPattern);

        jobHistory = undefined;
      });
    });
  });

  describe('new JobHistory page', () => {
    beforeEach(() => {
      cy.visit(`${jobHistoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('JobHistory');
    });

    it('should create an instance of JobHistory', () => {
      cy.get(`[data-cy="startDate"]`).type('2021-12-09T02:45').should('have.value', '2021-12-09T02:45');

      cy.get(`[data-cy="endDate"]`).type('2021-12-08T16:53').should('have.value', '2021-12-08T16:53');

      cy.get(`[data-cy="language"]`).select('ENGLISH');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        jobHistory = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', jobHistoryPageUrlPattern);
    });
  });
});
