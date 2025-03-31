import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPublisher, NewPublisher } from '../publisher.model';

export type PartialUpdatePublisher = Partial<IPublisher> & Pick<IPublisher, 'id'>;

export type EntityResponseType = HttpResponse<IPublisher>;
export type EntityArrayResponseType = HttpResponse<IPublisher[]>;

@Injectable({ providedIn: 'root' })
export class PublisherService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/publishers');

  create(publisher: NewPublisher): Observable<EntityResponseType> {
    return this.http.post<IPublisher>(this.resourceUrl, publisher, { observe: 'response' });
  }

  update(publisher: IPublisher): Observable<EntityResponseType> {
    return this.http.put<IPublisher>(`${this.resourceUrl}/${this.getPublisherIdentifier(publisher)}`, publisher, { observe: 'response' });
  }

  partialUpdate(publisher: PartialUpdatePublisher): Observable<EntityResponseType> {
    return this.http.patch<IPublisher>(`${this.resourceUrl}/${this.getPublisherIdentifier(publisher)}`, publisher, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPublisher>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPublisher[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPublisherIdentifier(publisher: Pick<IPublisher, 'id'>): number {
    return publisher.id;
  }

  comparePublisher(o1: Pick<IPublisher, 'id'> | null, o2: Pick<IPublisher, 'id'> | null): boolean {
    return o1 && o2 ? this.getPublisherIdentifier(o1) === this.getPublisherIdentifier(o2) : o1 === o2;
  }

  addPublisherToCollectionIfMissing<Type extends Pick<IPublisher, 'id'>>(
    publisherCollection: Type[],
    ...publishersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const publishers: Type[] = publishersToCheck.filter(isPresent);
    if (publishers.length > 0) {
      const publisherCollectionIdentifiers = publisherCollection.map(publisherItem => this.getPublisherIdentifier(publisherItem));
      const publishersToAdd = publishers.filter(publisherItem => {
        const publisherIdentifier = this.getPublisherIdentifier(publisherItem);
        if (publisherCollectionIdentifiers.includes(publisherIdentifier)) {
          return false;
        }
        publisherCollectionIdentifiers.push(publisherIdentifier);
        return true;
      });
      return [...publishersToAdd, ...publisherCollection];
    }
    return publisherCollection;
  }
}
