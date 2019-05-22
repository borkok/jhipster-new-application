import { IProfile } from 'app/shared/model/profile.model';

export interface IPerson {
  id?: number;
  name?: string;
  profile?: IProfile;
}

export const defaultValue: Readonly<IPerson> = {};
