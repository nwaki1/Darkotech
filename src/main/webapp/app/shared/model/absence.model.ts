import dayjs from 'dayjs';
import { IEmployee } from 'app/shared/model/employee.model';

export interface IAbsence {
  id?: number;
  imageContentType?: string | null;
  image?: string | null;
  time?: string | null;
  employee?: IEmployee | null;
}

export const defaultValue: Readonly<IAbsence> = {};
