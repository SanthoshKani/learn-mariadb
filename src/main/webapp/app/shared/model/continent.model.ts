export interface IContinent {
  id?: number;
  name?: string;
}

export class Continent implements IContinent {
  constructor(
    public id?: number,
    public name?: string,
  ) {}
}
