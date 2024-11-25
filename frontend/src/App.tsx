import '@mantine/core/styles.css';
import '@mantine/dates/styles.css';
import { MantineProvider } from '@mantine/core';
import { BrowserRouter } from 'react-router-dom';
import { BaseLayout }from './layout/BaseLayout.tsx';
import { AppRoutes } from './AppRoutes.tsx';
import './App.css'

function App() {

  return (
    <MantineProvider>
        <BrowserRouter>
          <BaseLayout>
            <AppRoutes />
          </BaseLayout>
        </BrowserRouter>
    </MantineProvider>
  )
}

export default App
