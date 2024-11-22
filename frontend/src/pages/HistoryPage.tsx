import { Badge, Card, Center, Container, Flex, Group, Stack, Title, Text } from '@mantine/core';
import React from 'react';
import EmptyData from '../components/EmptyData.tsx';
import Booking from '../interfaces/entities/booking.ts';
import { getColor } from '../utils/utils.ts';
import { getDateOnly, getHourAndMinutes } from '../utils/date-formater.ts';

export const HistoryPage = () => {

  const [bookings, setBookings] = React.useState<Booking[]>([]);

  React.useEffect(() => {
    const fetchData = () => {
      // const response = await fetch('http://localhost:3001/contracts');
      // const data = await response.json();
      setBookings(
        [
          {
            id: "1",
            startDate: new Date('2021-08-14T10:00:00'),
            endDate: new Date('2021-08-14T12:00:00'),
            status: 'finished',
            bookingType: 'conference-room-2',
          },
          {
            id: '3',
            startDate: new Date('2021-08-14T10:00:00'),
            endDate: new Date('2021-08-14T12:00:00'),
            status: 'cancelled',
            bookingType: 'conference-room-3',
          },
          {
            id: '3',
            startDate: new Date('2021-08-14T10:00:00'),
            endDate: new Date('2021-08-14T12:00:00'),
            status: 'ongoing',
            bookingType: 'conference-room-3',
          },
        ]
      );
    };
    fetchData();
  }, []);

  const BookingCard = (booking: Booking) => {
    return (
      <Card shadow="sm" padding="lg" radius="md" withBorder>
        <Group justify="space-between">
          <Badge bg={getColor(booking.status)} radius="sm">
            {booking.status}
          </Badge>
        </Group>

        <Stack mt="md" mb="xs">
          <Stack>
            <Group justify="space-between">
              <Title order={4}>Item</Title>
              <Title order={4}>{booking.bookingType}</Title>
            </Group>
            <Group justify="space-between">
              <Title order={4}>Date</Title>
              <Title order={4}>{getDateOnly(booking.startDate)}</Title>
            </Group>
          </Stack>
          <Stack>
            <Flex justify={'space-between'}>
              <Text size="sm">From: {getHourAndMinutes(booking.startDate)}</Text>
              <Text size="sm">To: {getHourAndMinutes(booking.endDate)}</Text>
            </Flex>
          </Stack>
        </Stack>
      </Card>

    )
  }

  return (
    <Container py={30}>
      <Stack>
        <Center>
          <Title order={3}>Your Bookings</Title>
        </Center>
        {bookings.length > 0 ? (
          bookings.map((booking) => <BookingCard key={booking.id} {...booking} />)
        ) : (
          <EmptyData message="Bookings" />
        )}
      </Stack>
    </Container>
  );
}

export default HistoryPage;