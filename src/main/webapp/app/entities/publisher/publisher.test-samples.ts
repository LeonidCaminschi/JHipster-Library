import { IPublisher, NewPublisher } from './publisher.model';

export const sampleWithRequiredData: IPublisher = {
  id: 25446,
  name: 'rapidly',
};

export const sampleWithPartialData: IPublisher = {
  id: 12608,
  name: 'a separately develop',
};

export const sampleWithFullData: IPublisher = {
  id: 519,
  name: 'odd',
};

export const sampleWithNewData: NewPublisher = {
  name: 'ha potable gift',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
