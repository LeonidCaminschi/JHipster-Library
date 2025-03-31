import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBookAuthor, NewBookAuthor } from '../book-author.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBookAuthor for edit and NewBookAuthorFormGroupInput for create.
 */
type BookAuthorFormGroupInput = IBookAuthor | PartialWithRequiredKeyOf<NewBookAuthor>;

type BookAuthorFormDefaults = Pick<NewBookAuthor, 'id'>;

type BookAuthorFormGroupContent = {
  id: FormControl<IBookAuthor['id'] | NewBookAuthor['id']>;
  author: FormControl<IBookAuthor['author']>;
  book: FormControl<IBookAuthor['book']>;
};

export type BookAuthorFormGroup = FormGroup<BookAuthorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookAuthorFormService {
  createBookAuthorFormGroup(bookAuthor: BookAuthorFormGroupInput = { id: null }): BookAuthorFormGroup {
    const bookAuthorRawValue = {
      ...this.getFormDefaults(),
      ...bookAuthor,
    };
    return new FormGroup<BookAuthorFormGroupContent>({
      id: new FormControl(
        { value: bookAuthorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      author: new FormControl(bookAuthorRawValue.author),
      book: new FormControl(bookAuthorRawValue.book),
    });
  }

  getBookAuthor(form: BookAuthorFormGroup): IBookAuthor | NewBookAuthor {
    return form.getRawValue() as IBookAuthor | NewBookAuthor;
  }

  resetForm(form: BookAuthorFormGroup, bookAuthor: BookAuthorFormGroupInput): void {
    const bookAuthorRawValue = { ...this.getFormDefaults(), ...bookAuthor };
    form.reset(
      {
        ...bookAuthorRawValue,
        id: { value: bookAuthorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookAuthorFormDefaults {
    return {
      id: null,
    };
  }
}
