import { type ICountry } from '@/shared/model/country.model';

export interface ICountryStats {
  id?: number;
  year?: number;
  population?: number | null;
  gdp?: number | null;
  country?: ICountry | null;
}

export class CountryStats implements ICountryStats {
  constructor(
    public id?: number,
    public year?: number,
    public population?: number | null,
    public gdp?: number | null,
    public country?: ICountry | null,
  ) {}
}
