import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 91847,
  postalCode: 'parsing',
};

export const sampleWithPartialData: ILocation = {
  id: 73848,
  streetAddress: 'Beauty open 1080p',
  postalCode: 'hard Slovakia Directives',
  stateProvince: 'navigating solutions Fresh',
};

export const sampleWithFullData: ILocation = {
  id: 14345,
  streetAddress: 'network',
  postalCode: 'Small',
  city: 'North Nathan',
  stateProvince: 'Israeli',
};

export const sampleWithNewData: NewLocation = {
  postalCode: 'Ball real-time',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
