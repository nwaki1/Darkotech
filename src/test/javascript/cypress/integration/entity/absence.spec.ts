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

describe('Absence e2e test', () => {
  const absencePageUrl = '/absence';
  const absencePageUrlPattern = new RegExp('/absence(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const absenceSample = {};

  let absence: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/absences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/absences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/absences/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (absence) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/absences/${absence.id}`,
      }).then(() => {
        absence = undefined;
      });
    }
  });

  it('Absences menu should load Absences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('absence');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Absence').should('exist');
    cy.url().should('match', absencePageUrlPattern);
  });

  describe('Absence page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(absencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Absence page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/absence/new$'));
        cy.getEntityCreateUpdateHeading('Absence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', absencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/absences',
          body: absenceSample,
        }).then(({ body }) => {
          absence = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/absences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [absence],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(absencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Absence page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('absence');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', absencePageUrlPattern);
      });

      it('edit button click should load edit Absence page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Absence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', absencePageUrlPattern);
      });

      it('last delete button click should delete instance of Absence', () => {
        cy.intercept('GET', '/api/absences/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('absence').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', absencePageUrlPattern);

        absence = undefined;
      });
    });
  });

  describe('new Absence page', () => {
    beforeEach(() => {
      cy.visit(`${absencePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Absence');
    });

    it('should create an instance of Absence', () => {
      cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="time"]`).type('2021-12-08T19:20').should('have.value', '2021-12-08T19:20');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        absence = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', absencePageUrlPattern);
    });
  });
});
