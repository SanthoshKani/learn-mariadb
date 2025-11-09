import { type IVip } from '@/shared/model/vip.model';
import { type IGuest } from '@/shared/model/guest.model';

export interface IEvent {
  id?: number;
  name?: string;
  date?: Date;
  vips?: IVip[] | null;
  guests?: IGuest[] | null;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public name?: string,
    public date?: Date,
    public vips?: IVip[] | null,
    public guests?: IGuest[] | null,
  ) {}
}
