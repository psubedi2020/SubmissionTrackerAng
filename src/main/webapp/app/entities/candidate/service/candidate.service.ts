import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICandidate, NewCandidate } from '../candidate.model';

export type PartialUpdateCandidate = Partial<ICandidate> & Pick<ICandidate, 'id'>;

type RestOf<T extends ICandidate | NewCandidate> = Omit<T, 'availableDate'> & {
  availableDate?: string | null;
};

export type RestCandidate = RestOf<ICandidate>;

export type NewRestCandidate = RestOf<NewCandidate>;

export type PartialUpdateRestCandidate = RestOf<PartialUpdateCandidate>;

export type EntityResponseType = HttpResponse<ICandidate>;
export type EntityArrayResponseType = HttpResponse<ICandidate[]>;

@Injectable({ providedIn: 'root' })
export class CandidateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/candidates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(candidate: NewCandidate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidate);
    return this.http
      .post<RestCandidate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(candidate: ICandidate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidate);
    return this.http
      .put<RestCandidate>(`${this.resourceUrl}/${this.getCandidateIdentifier(candidate)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(candidate: PartialUpdateCandidate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidate);
    return this.http
      .patch<RestCandidate>(`${this.resourceUrl}/${this.getCandidateIdentifier(candidate)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCandidate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCandidate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCandidateIdentifier(candidate: Pick<ICandidate, 'id'>): number {
    return candidate.id;
  }

  compareCandidate(o1: Pick<ICandidate, 'id'> | null, o2: Pick<ICandidate, 'id'> | null): boolean {
    return o1 && o2 ? this.getCandidateIdentifier(o1) === this.getCandidateIdentifier(o2) : o1 === o2;
  }

  addCandidateToCollectionIfMissing<Type extends Pick<ICandidate, 'id'>>(
    candidateCollection: Type[],
    ...candidatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const candidates: Type[] = candidatesToCheck.filter(isPresent);
    if (candidates.length > 0) {
      const candidateCollectionIdentifiers = candidateCollection.map(candidateItem => this.getCandidateIdentifier(candidateItem)!);
      const candidatesToAdd = candidates.filter(candidateItem => {
        const candidateIdentifier = this.getCandidateIdentifier(candidateItem);
        if (candidateCollectionIdentifiers.includes(candidateIdentifier)) {
          return false;
        }
        candidateCollectionIdentifiers.push(candidateIdentifier);
        return true;
      });
      return [...candidatesToAdd, ...candidateCollection];
    }
    return candidateCollection;
  }

  protected convertDateFromClient<T extends ICandidate | NewCandidate | PartialUpdateCandidate>(candidate: T): RestOf<T> {
    return {
      ...candidate,
      availableDate: candidate.availableDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCandidate: RestCandidate): ICandidate {
    return {
      ...restCandidate,
      availableDate: restCandidate.availableDate ? dayjs(restCandidate.availableDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCandidate>): HttpResponse<ICandidate> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCandidate[]>): HttpResponse<ICandidate[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
