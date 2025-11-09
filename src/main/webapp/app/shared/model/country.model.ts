import { type ILanguage } from '@/shared/model/language.model';
import { type IRegion } from '@/shared/model/region.model';

export interface ICountry {
  id?: number;
  name?: string;
  area?: number;
  nationalDay?: Date | null;
  countryCode2?: string;
  countryCode3?: string;
  languages?: ILanguage[] | null;
  region?: IRegion | null;
}

export class Country implements ICountry {
  constructor(
    public id?: number,
    public name?: string,
    public area?: number,
    public nationalDay?: Date | null,
    public countryCode2?: string,
    public countryCode3?: string,
    public languages?: ILanguage[] | null,
    public region?: IRegion | null,
  ) {}
}
