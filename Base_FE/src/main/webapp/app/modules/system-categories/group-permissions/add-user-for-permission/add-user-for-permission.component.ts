import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {TreeViewComponent} from "@syncfusion/ej2-angular-navigations";
import {NgbActiveModal, NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup} from "@angular/forms";
import {forkJoin, Observable, of, Subject} from "rxjs";
import {CommonService} from "app/shared/services/common.service";
import {ToastService} from "app/shared/services/toast.service";
import {NgxSpinnerService} from "ngx-spinner";
import {JhiEventManager} from "ng-jhipster";
import {HeightService} from "app/shared/services/height.service";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {GroupPermissionService} from "app/core/services/group-permission/group-permission.service";
import {debounceTime} from "rxjs/operators";
import {TIME_OUT} from "app/shared/constants/set-timeout.constants";
import {HumanResouces} from "app/core/models/human-resources/human-resouces.model";
import {SHOW_HIDE_COL_HEIGHT} from 'app/shared/constants/perfect-scroll-height.constants';
import {FormStoringService} from "app/shared/services/form-storing.service";

@Component({
  selector: 'jhi-add-user-for-permission',
  templateUrl: './add-user-for-permission.component.html',
  styleUrls: ['./add-user-for-permission.component.scss']
})
export class AddUserForPermissionComponent implements OnInit {

  @Input() type;
  @Input() data: any;
  @Input() groupPermissions: any[];
  @ViewChild('treevalidate', {static: false}) treevalidate: TreeViewComponent;

  @Input() item;
  SHOW_HIDE_COL_HEIGHT = SHOW_HIDE_COL_HEIGHT;
  name: any[];
  ngbModalRef: NgbModalRef;
  form: FormGroup;
  listUnit$ = new Observable<any[]>();
  unitSearch;
  debouncer: Subject<string> = new Subject<string>();
  positionList: any[] = [];
  itemsPerPage: any;
  height: number;
  page: number;
  field: Object;
  predicate: any;
  reverse: any;
  lstProjectPlan: any[] = [];
  buttonDisabled = false;
  humanResourceId: any[] = [];
  listDelete = [];
  listSave = [];
  listUserPermitted: HumanResouces[] = [];
  objClone: HumanResouces[] = [];
  isModalConfirmShow = false;
  userPermitted: number;
  disableAddNew = false;
  isAddNew = false;
  tempParcode: string;

  constructor(
    public activeModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private commonService: CommonService,
    private toastService: ToastService,
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private heightService: HeightService,
    private humanResourcesApiService: HumanResourcesApiService,
    private groupPermissionService: GroupPermissionService,
    private storageService: FormStoringService
  ) {
  }

  ngOnInit() {
    this.buildForm();
    this.debounceOnSearch();
    this.onResize();
  }

  debounceOnSearch() {
    this.debouncer.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit(value));
  }

  onSearchUnit(event) {
    this.unitSearch = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer.next(term);
    } else {
      this.listUnit$ = of([]);
    }
  }

  loadDataOnSearchUnit(term) {
    this.humanResourcesApiService
      .getlistHumanResourcesDepatment(term)
      .subscribe(res => {
        if (res.code === 'OK') {
          const dataRes: any = res.data;
          this.listUnit$ = of(dataRes.sort((a, b) => a.username.localeCompare(b.username)));
        } else {
          this.listUnit$ = of([]);
        }
      });
  }

  onClearUnit() {
    this.listUnit$ = of([]);
    this.unitSearch = '';
  }

  onSearchUnitClose() {
    this.listUnit$ = of([]);
    this.unitSearch = '';
  }

  deleteFieldValue(index) {
    if (this.listUserPermitted[index].username !== '') {
      this.isModalConfirmShow = true;
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'delete';
      modalRef.componentInstance.param = this.listUserPermitted[index].username;
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.listDelete.push(this.listUserPermitted[index].humanResourceId);
          this.listUserPermitted.splice(index, 1);
        }
        this.isModalConfirmShow = false;
      });
    } else {
      this.listUserPermitted.splice(index, 1);
    }
    this.disableAddNew = false;
  }

  onClickAddEffort() {
    const obj: any = {
      humanResourceId: -1,
      username: '',
      parcode: '',
      createBy: this.storageService.get('user').humanResourceId
    };
    this.disableAddNew = true;
    this.listUserPermitted.push(obj);
  }

  closeModal() {
    this.activeModal.dismiss('Cross click');
    this.activeModal.dismiss(true);
  }

  getSaveListUser() {
    this.listSave.push(this.data.id);
    for (let i = 0; i < this.listUserPermitted.length; i++) {
      if (this.userPermitted !== this.listUserPermitted[i].humanResourceId) {
        this.listSave.push(this.listUserPermitted[i].humanResourceId);
      }
    }
  }

  equals(obj1, obj2) {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }

  validData(){
    let checkValid = true;
    for (let i = 0; i < this.listUserPermitted.length; i++) {
      if (this.listUserPermitted[i].humanResourceId === -1) {
        checkValid = false;
        this.toastService.openErrorToast('Trường Mã nhân sự tại dòng ' + (i + 1) + ' không được để trống!!');
      }else{
        this.listUserPermitted[i].updateBy = this.storageService.get('user').humanResourceId;
      }
    }
    return checkValid;
  }

  onSubmitData(){
    if(this.validData()){
      this.isModalConfirmShow = true;
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'confirmModal';
      modalRef.componentInstance.message = 'Bạn có chắc chắn muốn ghi lại hành động?';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.getSaveListUser();
          this.activeModal.dismiss();
          this.listDelete.unshift(this.data.id);
          const saveReq = this.groupPermissionService.addUserPermission(this.listSave);
          const deleteReq = this.groupPermissionService.deleteUserPermission(this.listDelete);
          forkJoin([saveReq, deleteReq]).subscribe(
            res => {
              if (res[0].status === 200 && res[1].status === 200) {
                if (this.isAddNew === true) {
                  this.toastService.openSuccessToast('Thêm mới/cấp quyền cho thành viên thành công!');
                } else if (this.listUserPermitted.length === 0) {
                  this.toastService.openSuccessToast('Xóa quyền cho thành viên thành công!');
                } else {
                  this.toastService.openSuccessToast('Sửa quyền cho thành viên thành công!');
                }
              }
            }, error1 => {
              this.toastService.openErrorToast('Lỗi hệ thống!');
            });
        }
        this.isModalConfirmShow = false;
      });
    }
  }

  loadAll() {
    this.spinner.show();
    this.humanResourcesApiService
      .searchHumanResources({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        code: this.form.get('organizationId').value ? this.form.get('organizationId').value : '',
        parcode: this.form.get('positionName').value ? this.form.get('positionName').value : '',
        active: 1,
      })
      .subscribe(
        res => {
          this.spinner.hide();
          //this.paginateUserList(res.body);
        },
        err => {
          this.spinner.hide();
          // this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
        }
      );
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  changeData(item) {
    this.tempParcode = item.parcode;
    this.groupPermissionService.isUserPermitted(item.humanResourceId).subscribe(res => {
        if (item.humanResourceId === res.userId) {
          this.toastService.openErrorToast('Người dùng đã được thêm vào nhóm quyền ' + this.groupPermissions[res.groupPermissionId].name + '!');
          this.userPermitted = res.userId;
        }
      },
      err => {
      });
    this.disableAddNew = false;
  }

  private buildForm() {
    this.groupPermissionService.getListUserPermitted(this.data.id).subscribe(
      res => {
        if (res) {
          if (res.length === 0) {
            this.isAddNew = true;
          }
          for (let i = 0; i < res.length; i++) {
            this.listUserPermitted.push(res[i]);
            this.objClone.push(res[i]);
          }
        } else {
          this.listUserPermitted = [];
        }
      },
      err => {
        this.listUserPermitted = [];
      }
    );
  }

  onCloseAddModal() {
    if (!this.equals(this.listUserPermitted, this.objClone) && this.listUserPermitted[this.listUserPermitted.length - 1].humanResourceId !== -1) {
      this.isModalConfirmShow = true;
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'confirm';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.activeModal.dismiss();
        }
        this.isModalConfirmShow = false;
      });
    } else {
      this.activeModal.dismiss();
      this.activeModal.dismiss(true);
    }
  }

}
