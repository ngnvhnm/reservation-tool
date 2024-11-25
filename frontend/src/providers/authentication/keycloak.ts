import Keycloak from 'keycloak-js';

// Setup Keycloak instance as needed
// Pass initialization options as required or leave blank to load from 'keycloak.json'
const keycloak = new Keycloak(
  {
    realm: 'reservation-tool',
    url: 'http://localhost:8084',
    clientId: 'reservation',
  }
)

export default keycloak;