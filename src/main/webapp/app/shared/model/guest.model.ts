import { type IVip } from '@/shared/model/vip.model';
import { type IEvent } from '@/shared/model/event.model';

export interface IGuest {
  id?: number;
  name?: string;
  vips?: IVip[] | null;
  events?: IEvent[] | null;
}

export class Guest implements IGuest {
  constructor(
    public id?: number,
    public name?: string,
    public vips?: IVip[] | null,
    public events?: IEvent[] | null,
  ) {}
}
