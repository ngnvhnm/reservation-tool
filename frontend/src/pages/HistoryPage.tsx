import { Card, Center, Container, Flex, Group, Stack, Title, Text } from '@mantine/core';
import React from 'react';
import EmptyData from '../components/EmptyData.tsx';
import Booking from '../interfaces/entities/booking.ts';
import keycloak from '../providers/authentication/keycloak.ts';
import { getAllBookingTypeConferenceByUser } from '../utils/api/api-conference.ts';

export const HistoryPage = () => {
  const [userBookings, setUserBookings] = React.useState<Booking[]>([]);

  const getUserBookings = () => {
    const email = keycloak.tokenParsed?.email ?? '';

    if (!email) {
      console.error('No email found in token');
      return;
    }

    getAllBookingTypeConferenceByUser(email)
      .then((data) => {
        console.log(data);
        setUserBookings(data);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  React.useEffect(() => {
    getUserBookings();
  }, []);

  const BookingCard = (booking: Booking) => {
    return (
      <Card shadow="sm" padding="lg" radius="md" withBorder>
        <Stack mt="md" mb="xs">
          <Stack>
            <Group justify="space-between">
              <Title order={4}>Item</Title>
              <Title order={4}>{booking.conferenceType}</Title>
            </Group>
            <Group justify="space-between">
              <Title order={4}>Date</Title>
              <Title order={4}>{booking.startTime.toString()}</Title>
            </Group>
          </Stack>
          <Stack>
            <Flex justify={'space-between'}>
              <Text size="sm">From: {booking.startTime.toString()}</Text>
              <Text size="sm">To: {booking.endTime.toString()}</Text>
            </Flex>
          </Stack>
        </Stack>
      </Card>
    );
  };

  return (
    <Container py={30}>
      <Stack>
        <Center>
          <Title order={3}>Your Bookings</Title>
        </Center>
        {userBookings.length > 0 ? (
          userBookings.map((booking) => <BookingCard key={booking.id} {...booking} />)
        ) : (
          <EmptyData message="Bookings" />
        )}
      </Stack>
    </Container>
  );
};

export default HistoryPage;
