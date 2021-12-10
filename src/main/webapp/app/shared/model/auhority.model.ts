import { IPermission } from 'app/shared/model/permission.model';

export interface IAuhority {
  id?: number;
  auhorityName?: string | null;
  permissions?: IPermission[] | null;
}

export const defaultValue: Readonly<IAuhority> = {};
