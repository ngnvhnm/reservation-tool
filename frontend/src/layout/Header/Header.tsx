import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { IconLogout } from '@tabler/icons-react';
import { Button, Container, Group, Image, Tabs, useMantineTheme } from '@mantine/core';
import classes from './Header.module.css';
import keycloak from '../../providers/authentication/keycloak.ts';
const tabs = ['booking', 'history', 'settings'];

export function Header() {
  const theme = useMantineTheme();
  const items = tabs.map((tab) => (
    <Tabs.Tab value={tab} key={tab}>
      {tab.charAt(0).toUpperCase() + tab.slice(1)}
    </Tabs.Tab>
  ));

  const handleTabChange = (newTab: string | null) => {
    setActiveTab(newTab);
    navigate(`/${newTab}`);
  };

  const [activeTab, setActiveTab] = useState<string | null>('booking');
  const navigate = useNavigate();

  return (
    <div className={classes.header}>
      <Container className={classes.mainSection} size="md">
        <Group justify="space-between">
          <Image
            src={
              'https://www.dachpc.com/wp-content/uploads/2024/04/DACHPC_Logo_19_04_2024_color_flach.svg'
            }
          />
          <Button
            color={theme.colors.blue[6]}
            leftSection={<IconLogout size={16} stroke={1.5} />}
            onClick={() => {
              console.log('logout');
              keycloak.logout();
            }}
          >
            Logout
          </Button>
        </Group>
      </Container>
      <Container size="md">
        <Tabs
          value={activeTab}
          onChange={(newTab) => handleTabChange(newTab)}
          variant="outline"
          visibleFrom="sm"
          classNames={{
            root: classes.tabs,
            list: classes.tabsList,
            tab: classes.tab,
          }}
        >
          <Tabs.List>{items}</Tabs.List>
        </Tabs>
      </Container>
    </div>
  );
}
