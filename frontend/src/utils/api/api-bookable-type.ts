import loadEnv from '../load-env.ts';
import { headers } from './index.ts';
import { Message } from 'postcss';
import { ComboboxItemGroup } from '@mantine/core';

const route = `${loadEnv().SPRING_BACKEND_ADDRESS}/v1/reserve-types`;

export const getBookableTypes = async () => {
  const response = await fetch(route, {
    headers: { ...(await headers()) },
  });

  const data = (await response.json()) as unknown;
  if (!response.ok) {
    throw new Error((data as Message).message);
  }
  return data as ComboboxItemGroup[];
};
