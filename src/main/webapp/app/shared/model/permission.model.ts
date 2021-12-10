import { IAuhority } from 'app/shared/model/auhority.model';

export interface IPermission {
  id?: number;
  permissionName?: string | null;
  auhorities?: IAuhority[] | null;
}

export const defaultValue: Readonly<IPermission> = {};
