import { type IContinent } from '@/shared/model/continent.model';

export interface IRegion {
  id?: number;
  name?: string;
  continent?: IContinent | null;
}

export class Region implements IRegion {
  constructor(
    public id?: number,
    public name?: string,
    public continent?: IContinent | null,
  ) {}
}
