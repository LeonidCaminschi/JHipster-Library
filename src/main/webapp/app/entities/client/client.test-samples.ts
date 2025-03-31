import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 16289,
  firstName: 'Simi',
  lastName: 'Moga',
  address: 'which yeast devastation',
  phone: '0369623541',
};

export const sampleWithPartialData: IClient = {
  id: 13353,
  firstName: 'Geanina',
  lastName: 'Dragomir',
  address: 'ah',
  phone: '021584680',
};

export const sampleWithFullData: IClient = {
  id: 31496,
  firstName: 'Șerban',
  lastName: 'Gheorghiu',
  address: 'advocate off granular',
  phone: '0335315672',
};

export const sampleWithNewData: NewClient = {
  firstName: 'Gică',
  lastName: 'Simionescu',
  address: 'um',
  phone: '0358514427',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
