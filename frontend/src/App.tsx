import '@mantine/core/styles.css';
import '@mantine/dates/styles.css';
import { MantineProvider } from '@mantine/core';
import { BrowserRouter } from 'react-router-dom';
import { BaseLayout }from './layout/BaseLayout.tsx';
import { AppRoutes } from './AppRoutes.tsx';
import './App.css'

import keycloak from './providers/authentication/keycloak.ts';

import { ReactKeycloakProvider } from '@react-keycloak/web';
declare module '@react-keycloak/web';

function App() {
  return (
    <ReactKeycloakProvider authClient={keycloak} initOptions={{ onLoad: 'login-required'}}>
      <MantineProvider>
        <BrowserRouter>
          <BaseLayout>
            <AppRoutes />
          </BaseLayout>
        </BrowserRouter>
      </MantineProvider>
  </ReactKeycloakProvider>
);
}

export default App
