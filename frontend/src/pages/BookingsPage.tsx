import React, { useEffect, useRef } from 'react';
import { DateInput, TimeInput } from '@mantine/dates';
import { useState } from 'react';
import {
  ActionIcon,
  Container,
  rem,
  Select,
  Button,
  Group,
  ComboboxItemGroup, Badge, Input,
} from '@mantine/core';
import { isEmail, useForm } from '@mantine/form';
import { IconClock, IconX } from '@tabler/icons-react';
import { getBookableTypes } from '../utils/api/api-bookable-type.ts';
import {
  createBookingTypeConference,
  CreateBookingTypeConferenceDto,
} from '../utils/api/api-conference.ts';
import { getFullDateGMT1 } from '../utils/date-formater.ts';
import { notifications } from '@mantine/notifications';
import keycloak from '../providers/authentication/keycloak.ts';

type FormValues = {
  bookingDate: Date | null;
  selectedItem: string | null;
  selectedStartTime: string | null;
  selectedEndTime: string | null;
  attendeeList: string[] | null;
  attendeeTemp: string | null;
};

const initialValues: FormValues = {
  bookingDate: null,
  selectedItem: null,
  selectedStartTime: null,
  selectedEndTime: null,
  attendeeList: null,
  attendeeTemp: null,
};

export const BookingsPage = () => {
  const startTimeRef = useRef<HTMLInputElement>(null);
  const endTimeRef = useRef<HTMLInputElement>(null);
  const [availableBookableTypes, setAvailableBookableTypes] = useState<ComboboxItemGroup[]>([]);

  const getBookableItems = () => {
    getBookableTypes()
      .then((data) => {
        console.log(data);
        setAvailableBookableTypes(data);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const pickerControl = (inputRef: React.RefObject<HTMLInputElement>) => (
    <ActionIcon variant="subtle" color="gray" onClick={() => inputRef.current?.showPicker()}>
      <IconClock style={{ width: rem(16), height: rem(16) }} stroke={1.5} />
    </ActionIcon>
  );

  const form = useForm<FormValues>({
    mode: 'uncontrolled',
    initialValues: initialValues,
    validate: {
      bookingDate: (value) => {
        if (value == null) {
          return 'Booking date is required';
        }
      },
      selectedItem: (value) => {
        if (value == null) {
          return 'Booking type is required';
        }
      },
      selectedStartTime: (value) => {
        if (value === null) {
          return 'Start time is required';
        }
      },
      selectedEndTime: (value, values) => {
        if (value === null) {
          return 'End time is required';
        }
        if (
          values.selectedStartTime &&
          new Date(`1970-01-01T${values.selectedStartTime}`) >= new Date(`1970-01-01T${value}`)
        ) {
          return 'End time should be greater than start time';
        }
      },
      attendeeTemp: (value) => {
        return isEmail(value) ? undefined : 'Invalid email';
      }
    },
  });

  const onSubmit = (values: FormValues, event: React.FormEvent<HTMLFormElement> | undefined) => {
    event?.preventDefault();

    console.log(values);

    // TODO: Remove scope for better type checking and visibility
    const body: CreateBookingTypeConferenceDto = {
      startTime:
        values.bookingDate && values.selectedStartTime
          ? getFullDateGMT1(values.bookingDate, values.selectedStartTime)
          : new Date(),
      endTime:
        values.bookingDate && values.selectedEndTime
          ? getFullDateGMT1(values.bookingDate, values.selectedEndTime)
          : new Date(),
      conferenceType: values.selectedItem ?? '',
      bookerEmail: keycloak.tokenParsed?.email ?? '',
      attendeeList: values.attendeeList ? values.attendeeList.join(',') : '',
    };

    createBookingTypeConference(body)
      .then(() => {
        // TODO: Create util for this, make more abstraction
        console.log('Booking created');
        notifications.show({
          title: 'Success',
          message: 'Booking created successfully',
          color: 'green',
        });
      })
      .catch((error) => {
        // TODO: Create util for this, make more abstraction
        console.error(error);
        notifications.show({
          title: 'Error',
          message: error.message,
          color: 'red',
        });
      })
      .finally(() => {
        form.reset();
      });
  };

  useEffect(() => {
    getBookableItems();
  }, []);

  return (
    <Container py={30} size={'xl'}>
      <form onSubmit={form.onSubmit(onSubmit)}>
        <DateInput
          minDate={new Date()}
          maxDate={new Date(new Date().setDate(new Date().getDate() + 30))}
          valueFormat="DD. MMM YYYY "
          label="Booking Date"
          placeholder="Select your booking date"
          required
          key={form.key('bookingDate')}
          {...form.getInputProps('bookingDate')}
        />
        <Select
          label="Select your booking type"
          description={form.getInputProps('bookingDate').value}
          placeholder="Parking Lot or Meeting Room"
          data={availableBookableTypes}
          required
          key={form.key('selectedItem')}
          {...form.getInputProps('selectedItem')}
          disabled={form.getValues().bookingDate === null}
        />
        <TimeInput
          label="Select your start time"
          ref={startTimeRef}
          rightSection={pickerControl(startTimeRef)}
          variant="filled"
          withAsterisk
          required
          key={form.key('selectedStartTime')}
          {...form.getInputProps('selectedStartTime')}
          disabled={form.getValues().selectedItem === null || form.getValues().bookingDate === null}
        />
        <TimeInput
          minTime={form.getValues()?.selectedStartTime ?? '00:00'}
          label="Select your start time"
          ref={endTimeRef}
          rightSection={pickerControl(endTimeRef)}
          variant="filled"
          withAsterisk
          required
          key={form.key('selectedEndTime')}
          {...form.getInputProps('selectedEndTime')}
          disabled={
            form.getValues().selectedItem === null ||
            form.getValues().bookingDate === null ||
            form.getValues().selectedStartTime === null
          }
        />
        <Group mt="md" bg="gray" p="md">
          {
            form.getValues().attendeeList?.map((attendee, index) => (
              <Badge key={index} color="blue" variant="filled">
                {attendee}
                <IconX size={10}
                       style={{ cursor: 'pointer' }}
                       onClick={() => {
                  const values = form.getValues();
                  form.setFieldValue(
                    'attendeeList',
                    values?.attendeeList
                      ? values.attendeeList.filter((_, i) => i !== index)
                      : [], // Fallback to an empty array
                  );
                }} />
              </Badge>))
          }
          <Input
            type={'email'}
            placeholder="Enter attendees"
            key={form.key('attendeeTemp')}
            {...form.getInputProps('attendeeTemp')}
            onKeyPress={(event) => {
              console.log("value2: " + event.currentTarget.value);

              if (event.key === 'Enter' || event.key === ',' || event.key === ' ') {
                form.setFieldValue('attendeeList', [
                  ...(form.getValues().attendeeList ?? []),
                  event.currentTarget.value,
                ]);
                console.log("value: " + form.getValues().attendeeList);
                event.currentTarget.value = '';
              }
            }}
            disabled={
              form.getValues().selectedItem === null ||
              form.getValues().bookingDate === null ||
              form.getValues().selectedStartTime === null ||
              form.getValues().selectedEndTime === null
            }
          >
          </Input>
        </Group>
        <Group justify="flex-end" mt="md">
          <Button
            type="submit"
            color="blue"
            disabled={
              form.getValues().selectedItem === null ||
              form.getValues().bookingDate === null ||
              form.getValues().selectedStartTime === null ||
              form.getValues().selectedEndTime === null
            }
          >
            Submit
          </Button>
        </Group>
      </form>
    </Container>
  );
};
