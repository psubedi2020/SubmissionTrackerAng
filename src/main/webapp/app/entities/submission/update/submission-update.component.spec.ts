import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SubmissionFormService } from './submission-form.service';
import { SubmissionService } from '../service/submission.service';
import { ISubmission } from '../submission.model';
import { ICandidate } from 'app/entities/candidate/candidate.model';
import { CandidateService } from 'app/entities/candidate/service/candidate.service';

import { SubmissionUpdateComponent } from './submission-update.component';

describe('Submission Management Update Component', () => {
  let comp: SubmissionUpdateComponent;
  let fixture: ComponentFixture<SubmissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let submissionFormService: SubmissionFormService;
  let submissionService: SubmissionService;
  let candidateService: CandidateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SubmissionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SubmissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubmissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    submissionFormService = TestBed.inject(SubmissionFormService);
    submissionService = TestBed.inject(SubmissionService);
    candidateService = TestBed.inject(CandidateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Candidate query and add missing value', () => {
      const submission: ISubmission = { id: 456 };
      const candidate: ICandidate = { id: 18876 };
      submission.candidate = candidate;

      const candidateCollection: ICandidate[] = [{ id: 66342 }];
      jest.spyOn(candidateService, 'query').mockReturnValue(of(new HttpResponse({ body: candidateCollection })));
      const additionalCandidates = [candidate];
      const expectedCollection: ICandidate[] = [...additionalCandidates, ...candidateCollection];
      jest.spyOn(candidateService, 'addCandidateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(candidateService.query).toHaveBeenCalled();
      expect(candidateService.addCandidateToCollectionIfMissing).toHaveBeenCalledWith(
        candidateCollection,
        ...additionalCandidates.map(expect.objectContaining)
      );
      expect(comp.candidatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const submission: ISubmission = { id: 456 };
      const candidate: ICandidate = { id: 440 };
      submission.candidate = candidate;

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(comp.candidatesSharedCollection).toContain(candidate);
      expect(comp.submission).toEqual(submission);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 123 };
      jest.spyOn(submissionFormService, 'getSubmission').mockReturnValue(submission);
      jest.spyOn(submissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submission }));
      saveSubject.complete();

      // THEN
      expect(submissionFormService.getSubmission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(submissionService.update).toHaveBeenCalledWith(expect.objectContaining(submission));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 123 };
      jest.spyOn(submissionFormService, 'getSubmission').mockReturnValue({ id: null });
      jest.spyOn(submissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submission }));
      saveSubject.complete();

      // THEN
      expect(submissionFormService.getSubmission).toHaveBeenCalled();
      expect(submissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 123 };
      jest.spyOn(submissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(submissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCandidate', () => {
      it('Should forward to candidateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(candidateService, 'compareCandidate');
        comp.compareCandidate(entity, entity2);
        expect(candidateService.compareCandidate).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
