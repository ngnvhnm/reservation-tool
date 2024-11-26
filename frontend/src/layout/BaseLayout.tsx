import { AppShell, rem } from '@mantine/core';
import { sizes } from '../utils/constants';
import { Header } from './Header/Header';
import { ReactNode } from 'react';

export const BaseLayout = ({ children }: { children: ReactNode }) => {
  return (
    <AppShell>
      <AppShell.Header h={sizes.headerHeight}>
        <Header />
      </AppShell.Header>

      <AppShell.Main
        pt={sizes.headerHeight}
        h={`calc(100vh - ${rem(sizes.headerHeight)})`}
        style={{ overflowY: 'auto' }}
      >
        {children}
      </AppShell.Main>
    </AppShell>
  );
};
