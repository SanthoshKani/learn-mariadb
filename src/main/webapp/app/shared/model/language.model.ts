import { type ICountry } from '@/shared/model/country.model';

export interface ILanguage {
  id?: number;
  language?: string;
  countries?: ICountry[] | null;
}

export class Language implements ILanguage {
  constructor(
    public id?: number,
    public language?: string,
    public countries?: ICountry[] | null,
  ) {}
}
