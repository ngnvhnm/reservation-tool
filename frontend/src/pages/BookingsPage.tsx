import { useRef } from 'react';
import { DateInput, TimeInput } from '@mantine/dates';
import { useState } from 'react';
import { ActionIcon, Container, rem, Select, Button, Group } from '@mantine/core';
import { useForm } from '@mantine/form';
import { IconClock } from '@tabler/icons-react';

export const BookingsPage = () => {
  const [value, setValue] = useState<Date | null>(null);
  const ref = useRef<HTMLInputElement>(null);

  const pickerControl = (
    <ActionIcon variant="subtle" color="gray" onClick={() => ref.current?.showPicker()}>
      <IconClock style={{ width: rem(16), height: rem(16) }} stroke={1.5} />
    </ActionIcon>
  );

  const form = useForm({
    initialValues: {
      date: new Date(),
      time: new Date(),
    },
  });


  return (
    <Container py={30} size={'xl'}>
      <form onSubmit={form.onSubmit(console.log)}>
        <DateInput
          value={value}
          onChange={setValue}
          label="Date input"
          placeholder="Date input"
        />
        <Select
          label="Select your booking type"
          placeholder="Pick value"
          data={[
            { group: 'Conference Room', items: ['Unten', 'Oben 1', 'Oben 2'] },
            {
              group: 'Parkplatz',
              items: ['Parkplatz 1', 'Parkplatz 2', 'Parkplatz 3', 'Parkplatz 4', 'Parkplatz 5', 'Parkplatz 6', 'Parkplatz 7']
            },
          ]}
        />
        <TimeInput label="Click icon to show browser picker" ref={ref}
                   rightSection={pickerControl} />
        <TimeInput label="Click icon to show browser picker" ref={ref}
                   rightSection={pickerControl} />
        <Group justify="flex-end" mt="md">
          <Button type="submit">Submit</Button>
        </Group>
      </form>
    </Container>
);
}