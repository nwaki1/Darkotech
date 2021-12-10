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

describe('Auhority e2e test', () => {
  const auhorityPageUrl = '/auhority';
  const auhorityPageUrlPattern = new RegExp('/auhority(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const auhoritySample = {};

  let auhority: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/auhorities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/auhorities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/auhorities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (auhority) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/auhorities/${auhority.id}`,
      }).then(() => {
        auhority = undefined;
      });
    }
  });

  it('Auhorities menu should load Auhorities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('auhority');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Auhority').should('exist');
    cy.url().should('match', auhorityPageUrlPattern);
  });

  describe('Auhority page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(auhorityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Auhority page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/auhority/new$'));
        cy.getEntityCreateUpdateHeading('Auhority');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', auhorityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/auhorities',
          body: auhoritySample,
        }).then(({ body }) => {
          auhority = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/auhorities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [auhority],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(auhorityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Auhority page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('auhority');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', auhorityPageUrlPattern);
      });

      it('edit button click should load edit Auhority page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Auhority');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', auhorityPageUrlPattern);
      });

      it('last delete button click should delete instance of Auhority', () => {
        cy.intercept('GET', '/api/auhorities/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('auhority').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', auhorityPageUrlPattern);

        auhority = undefined;
      });
    });
  });

  describe('new Auhority page', () => {
    beforeEach(() => {
      cy.visit(`${auhorityPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Auhority');
    });

    it('should create an instance of Auhority', () => {
      cy.get(`[data-cy="auhorityName"]`).type('Borders Frozen').should('have.value', 'Borders Frozen');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        auhority = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', auhorityPageUrlPattern);
    });
  });
});
