import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {OrganizationCategoriesComponent} from './organization-categories/organization-categories.component';
import {SystemCategoriesRoutingModule} from 'app/modules/system-categories/system-categories-routing.module';
import {InvoiceWebappSharedModule} from 'app/shared/shared.module';
import {PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {BarChartComponent} from './organization-categories/bar-chart/bar-chart.component';
import {ChartsModule} from 'ng2-charts';
import {AddUserComponent} from './add-user/add-user.component';
import {ViewUserComponent} from './view-user/view-user.component';
import {MasterplanComponent} from './masterplan/masterplan.component';
import {ListHandoverProductComponent} from "./project-management/list-handover-product/list-handover-product.component";
import {ProjectManagementComponent} from './project-management/project-management.component';
import {LinkRedmineComponent} from "app/modules/system-categories/view-user/linkRedmine/linkRedmine.component";
import {ConvertStatusPipe} from "app/shared/pipes/convert-status.pipe";
import {MaxLengthTextPipe} from "app/shared/pipes/max-length-text.pipe";
import {GroupPermissionsComponent} from './group-permissions/group-permissions.component';
import {SaveGroupPermissionComponent} from './group-permissions/save-group-permission/save-group-permission.component';
import {AddUserForPermissionComponent} from './group-permissions/add-user-for-permission/add-user-for-permission.component';
import {TreeViewModule} from '@syncfusion/ej2-angular-navigations';
import {HumanResourcesComponent} from "app/modules/system-categories/human-resources/human-resources.component";
import {AddHumanResourcesComponent} from "app/modules/system-categories/human-resources/add-human-resources/add-human-resources.component";
import {ModalConfirmComponent} from './masterplan/modal-confirm/modal-confirm.component';
import {AddHumanComponent} from './project-management/add-human/add-human.component';
import { ModalComponent } from './organization-categories/modal/modal.component';
import {FormStoringService} from "app/shared/services/form-storing.service";
@NgModule({
  declarations: [
    OrganizationCategoriesComponent,
    BarChartComponent,
    AddUserComponent,
    ViewUserComponent,
    MasterplanComponent,
    ListHandoverProductComponent,
    ProjectManagementComponent,
    LinkRedmineComponent,
    GroupPermissionsComponent,
    SaveGroupPermissionComponent,
    AddUserForPermissionComponent,
    HumanResourcesComponent,
    AddHumanResourcesComponent,
    ModalConfirmComponent,
    AddHumanComponent,
    ModalComponent,

  ],
  imports: [CommonModule, SystemCategoriesRoutingModule, PerfectScrollbarModule, InvoiceWebappSharedModule, ChartsModule, TreeViewModule],
  entryComponents: [LinkRedmineComponent, SaveGroupPermissionComponent,
    AddUserForPermissionComponent, AddHumanResourcesComponent, ModalConfirmComponent, AddHumanComponent,ModalComponent],
  providers: [ConvertStatusPipe, MaxLengthTextPipe, FormStoringService]
})
export class SystemCategoriesModule {
}
