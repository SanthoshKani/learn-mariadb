import { type IGuest } from '@/shared/model/guest.model';
import { type IEvent } from '@/shared/model/event.model';

export interface IVip {
  id?: number;
  name?: string;
  guests?: IGuest[] | null;
  events?: IEvent[] | null;
}

export class Vip implements IVip {
  constructor(
    public id?: number,
    public name?: string,
    public guests?: IGuest[] | null,
    public events?: IEvent[] | null,
  ) {}
}
