import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';
import {OrganizationCategoriesComponent} from 'app/modules/system-categories/organization-categories/organization-categories.component';
import {AddUserComponent} from './add-user/add-user.component';
import {ViewUserComponent} from './view-user/view-user.component';
import {MasterplanComponent} from './masterplan/masterplan.component';
import {ProjectManagementComponent} from './project-management/project-management.component';
import {HumanResourcesComponent} from "app/modules/system-categories/human-resources/human-resources.component";
import {AddHumanResourcesComponent} from "app/modules/system-categories/human-resources/add-human-resources/add-human-resources.component";
import {GroupPermissionsComponent} from "app/modules/system-categories/group-permissions/group-permissions.component";
import {ListHandoverProductComponent} from "app/modules/system-categories/project-management/list-handover-product/list-handover-product.component";
import {AddHumanComponent} from './project-management/add-human/add-human.component';
import {UserActiveGuard} from "app/core/auth/user-active.guard";

const routes: Routes = [
  {
    path: 'add-human',
    component: AddHumanComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/add-human'
    }
  },
  {
    path: 'add-user',
    component: AddUserComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/add-user'
    }
  },
  {
    path: 'organization-categories',
    component: OrganizationCategoriesComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/organization-categories/:id'
    }
  },
  {
    path: 'view-user',
    component: ViewUserComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/view-user/:id'
    }
  },
  {
    path: 'project-management',
    component: ProjectManagementComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/project-management'
    }
  },
  {
    path: 'masterplan',
    component: MasterplanComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/masterplan/:id'
    }
  }, {
    path: 'human-resources',
    component: HumanResourcesComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/human-resources'
    }
  },
  {
    path: 'c',
    component: AddHumanResourcesComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/human-resources-add'
    }
  },
  {
    path: 'group-permissions',
    component: GroupPermissionsComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/group-permissions'
    }
  },
  {
    path: 'project-management/list-handover-product',
    component: ListHandoverProductComponent,
    canActivate: [UserActiveGuard],
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'organizationCategories.title',
      url: 'system-categories/project-management/list-handover-product/:id'
    }
  }


  // {
  //   path: 'add-user',
  //   component: AddUserComponent,
  //   canActivate: [],
  //   resolve: {
  //     pagingParams: JhiResolvePagingParams
  //   },
  //   data: {
  //     defaultSort: 'id,asc',
  //     pageTitle: 'AddUser.title',
  //     url: 'system-categories/add-user'
  //   }
  // },
  // {
  //   path: 'user-group-categories',
  //   component: UserGroupCategoriesComponent,
  //   canActivate: [],
  //   resolve: {
  //     pagingParams: JhiResolvePagingParams
  //   },
  //   data: {
  //     defaultSort: 'id,asc',
  //     pageTitle: 'userGroup.title',
  //     url: 'system-categories/user-group-categories'
  //   }
  // },
  // {
  //   path: 'data-categories',
  //   component: DataCategoriesComponent,
  //   canActivate: [],
  //   resolve: {
  //     pagingParams: JhiResolvePagingParams
  //   },
  //   data: {
  //     defaultSort: 'id,asc',
  //     pageTitle: 'dataCategories.webTitle',
  //     url: 'system-categories/data-categories'
  //   }
  // },
  // {
  //   path: 'sys-user',
  //   component: SysUserComponent,
  //   canActivate: [],
  //   resolve: {
  //     pagingParams: JhiResolvePagingParams
  //   },
  //   data: {
  //     defaultSort: 'id,asc',
  //     pageTitle: 'user.title',
  //     url: 'system-categories/sys-user'
  //   }
  // }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SystemCategoriesRoutingModule {
}
