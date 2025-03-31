import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBookAuthor } from '../book-author.model';
import { BookAuthorService } from '../service/book-author.service';

const bookAuthorResolve = (route: ActivatedRouteSnapshot): Observable<null | IBookAuthor> => {
  const id = route.params.id;
  if (id) {
    return inject(BookAuthorService)
      .find(id)
      .pipe(
        mergeMap((bookAuthor: HttpResponse<IBookAuthor>) => {
          if (bookAuthor.body) {
            return of(bookAuthor.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bookAuthorResolve;
