import { IBook, NewBook } from './book.model';

export const sampleWithRequiredData: IBook = {
  id: 3991,
  name: 'blue regal',
  publishYear: 'nocturnal wordy',
  picture: 'for as',
};

export const sampleWithPartialData: IBook = {
  id: 20784,
  name: 'blah legislature',
  publishYear: 'upwardly',
  picture: 'matter exploration secondary',
};

export const sampleWithFullData: IBook = {
  id: 8637,
  name: 'sniff',
  publishYear: 'however quicker',
  copies: 24504,
  picture: 'eek',
};

export const sampleWithNewData: NewBook = {
  name: 'taxicab nor',
  publishYear: 'agreeable boo',
  picture: 'boyfriend curiously although',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
