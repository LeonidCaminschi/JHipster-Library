import dayjs from 'dayjs/esm';

import { IBorrowedBook, NewBorrowedBook } from './borrowed-book.model';

export const sampleWithRequiredData: IBorrowedBook = {
  id: 27089,
  borrowDate: dayjs('2025-03-31T02:22'),
};

export const sampleWithPartialData: IBorrowedBook = {
  id: 22378,
  borrowDate: dayjs('2025-03-30T16:21'),
};

export const sampleWithFullData: IBorrowedBook = {
  id: 29509,
  borrowDate: dayjs('2025-03-30T12:37'),
};

export const sampleWithNewData: NewBorrowedBook = {
  borrowDate: dayjs('2025-03-30T13:09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
