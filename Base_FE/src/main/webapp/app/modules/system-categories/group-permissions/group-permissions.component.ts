import {Component, OnDestroy, OnInit} from '@angular/core';
import {GroupPermissionService} from "app/core/services/group-permission/group-permission.service";
import {HeightService} from "app/shared/services/height.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NgxSpinnerService} from "ngx-spinner";
import {JhiEventManager} from "ng-jhipster";
import {ToastService} from "app/shared/services/toast.service";
import {TranslateService} from "@ngx-translate/core";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from "app/shared/constants/pagination.constants";
import {Subscription} from "rxjs";
import {SaveGroupPermissionComponent} from "app/modules/system-categories/group-permissions/save-group-permission/save-group-permission.component";
import {AddUserForPermissionComponent} from "app/modules/system-categories/group-permissions/add-user-for-permission/add-user-for-permission.component";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {CommonService} from "app/shared/services/common.service";
import {map} from "rxjs/operators";

@Component({
  selector: 'jhi-group-permissions',
  templateUrl: './group-permissions.component.html',
  styleUrls: ['./group-permissions.component.scss']
})
export class GroupPermissionsComponent implements OnInit, OnDestroy {
  token = '';
  form: FormGroup;
  groupPermissions: any = [];
  departments: any = [];
  listStatus: any = [{name: '--Tất cả--', value: ''}, {name: 'Hoạt động', value: 'ACTIVE'}, {
    name: 'Không hoạt động',
    value: 'INACTIVE'
  }]
  listRole = [];
  code: any = {code: '', name: '--Tất cả--'};
  status: any = {name: 'Tất cả', value: ''};
  routeData: any;
  height: any;
  itemsPerPage: any;
  totalItems: any;
  maxSizePage: any;
  page: any;
  pagingParams
  previousPage: any;
  predicate: any;
  reverse: any;
  eventSubscriber: Subscription;

  constructor(
    private gpService: GroupPermissionService,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private toastService: ToastService,
    private translateService: TranslateService,
    private modalService: NgbModal,
    private commonService: CommonService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.maxSizePage = MAX_SIZE_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      if (data && data.pagingParams) {
        this.page = data.pagingParams.page;
        this.previousPage = data.pagingParams.page;
        this.reverse = data.pagingParams.ascending;
        this.predicate = data.pagingParams.predicate;
      }
    });
  }

  ngOnInit() {
    this.buidForm();
    this.loadAll();
    this.onResize();
    this.registerChange();
    this.getListRole();
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  changePageSize(size) {
    this.itemsPerPage = size;
    this.transition();
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('groupPermissionsChange', response => this.loadAll());
  }

  transition() {
    this.router.navigate(['/system-categories/group-permissions'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
        code: this.form.get('code').value ? this.form.get('code').value : '',
        status: this.form.get('status').value ? this.form.get('status').value : '',
        name: this.form.get('name').value ? this.form.get('name').value : ''
      }
    });
    this.loadAll();
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  onSearchData() {
    this.transition();
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  loadAll() {
    this.spinner.show();
    this.gpService
      .findAll({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        code: this.form.get('code').value ? this.form.get('code').value : '',
        status: this.form.get('status').value ? this.form.get('status').value : '',
        name: this.form.get('name').value ? this.form.get('name').value : ''
      })
      .subscribe(
        res => {
          this.spinner.hide();
          const d: any = res;
          this.paginateGroupPermissionList(d.data);
        },
        err => {
          this.spinner.hide();
          this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
        }
      );
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  onClearStatus() {
    this.setValueToField('status', '');
    this.loadAll();
  }

  openModalAddUser(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddUserForPermissionComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.data = selectedData;
    modalRef.componentInstance.groupPermissions = this.listRole;
    modalRef.result.then(result => {
    }).catch(() => {
    });
  }

  permissionCheck(permission?: string) {
    return this.commonService.havePermission(permission);
  }

  openModalSaveGroupPermissions(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(SaveGroupPermissionComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.data = selectedData;
    modalRef.result.then(result => {
      this.loadAll();
    }).catch(() => {
      this.loadAll();
    });
  }

  onDelete(ids) {
    this.gpService.checkDeleteGroupPermission({id: ids}).subscribe(res => {
      if (res) {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'delete';
        modalRef.componentInstance.param = 'nhóm quyền';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.onSubmitDelete(ids);
          }
        });
      } else {
        this.toastService.openErrorToast(this.translateService.instant('user.invalidDelete'));
      }
    });
  }

  onSubmitDelete(id) {
    this.spinner.show();
    this.gpService.delete(id).subscribe(
      res => {
        this.spinner.hide();
        this.toastService.openSuccessToast('Xóa nhóm quyền thành công');
        this.loadAll();
      },
      err => {
        this.spinner.hide();
        this.toastService.openErrorToast('Xóa nhóm quyền không thành công');
      }
    );
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      code: ['--Tất cả--'],
      name: [''],
      status: [''],
    });
  }

  private paginateGroupPermissionList(data) {
    this.totalItems = data.totalItems;
    this.groupPermissions = data.data;
  }

  private getListRole() {
    this.gpService.findListRole().subscribe(
      res => {
        const d: any = res;
        const all = {code: '', name: '--Tất cả--'}
        this.listRole = d.data;
        this.listRole = [all,...this.listRole];
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }
}
