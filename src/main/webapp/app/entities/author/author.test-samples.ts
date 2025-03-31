import { IAuthor, NewAuthor } from './author.model';

export const sampleWithRequiredData: IAuthor = {
  id: 24433,
  firstName: 'Maia',
  lastName: 'Baciu',
};

export const sampleWithPartialData: IAuthor = {
  id: 12726,
  firstName: 'Adonis',
  lastName: 'Ispas',
};

export const sampleWithFullData: IAuthor = {
  id: 16232,
  firstName: 'Ioanina',
  lastName: 'Ardeleanu',
};

export const sampleWithNewData: NewAuthor = {
  firstName: 'Octavian',
  lastName: 'Stoica',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
