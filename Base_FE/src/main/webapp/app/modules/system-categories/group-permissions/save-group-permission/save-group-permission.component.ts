import {Component, Input, OnInit} from '@angular/core';
import {HeightService} from "app/shared/services/height.service";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbActiveModal, NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {NgxSpinnerService} from "ngx-spinner";
import {TranslateService} from "@ngx-translate/core";
import {CommonService} from "app/shared/services/common.service";
import {ToastService} from "app/shared/services/toast.service";
import {JhiEventManager} from "ng-jhipster";
import {REGEX_PATTERN} from "app/shared/constants/pattern.constants";
import {SysUserService} from "app/core/services/system-management/sys-user.service";
import {GroupPermissionService} from "app/core/services/group-permission/group-permission.service";
import {TreeviewConfig, TreeviewItem} from 'ngx-treeview';
import {Permission} from "app/core/models/group-permission/permission";
import {enableRipple} from '@syncfusion/ej2-base';

enableRipple(true);

@Component({
  selector: 'jhi-save-group-permission',
  templateUrl: './save-group-permission.component.html',
  styleUrls: ['./save-group-permission.component.scss']
})
export class SaveGroupPermissionComponent implements OnInit {
  @Input() type;
  @Input() data: any;
  listRole: any[];
  ngbModalRef: NgbModalRef;
  form: FormGroup;
  height: number;
  statusList: any[] = [{name: 'Hoạt động', value: 'ACTIVE'}, {name: 'Không hoạt động', value: 'INACTIVE'}];
  permissionAll: Permission[];
  dropdownEnabled = true;
  items: TreeviewItem[] = [];
  checkedTreeView: any[] = [];
  checkIdOld: any[] = [];
  values: number[];

  config = TreeviewConfig.create({
    hasAllCheckBox: true,
    hasFilter: false,
    hasCollapseExpand: true,
    decoupleChildFromParent: false,
    maxHeight: 300
  });

  constructor(
    public activeModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private commonService: CommonService,
    private toastService: ToastService,
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private heightService: HeightService,
    private sysUserService: SysUserService,
    private translateService: TranslateService,
    private groupPermissionService: GroupPermissionService
  ) {
    this.height = this.heightService.onResize();
  }

  get formControl() {
    return this.form.controls;
  }

  ngOnInit() {
    this.getAllPermission();
    this.buildForm();
    this.getListRole();

  }

  setDataDefault() {
    this.form.patchValue(this.data);
  }
  private getListRole() {
    this.groupPermissionService.findListRole().subscribe(
      res => {
        const d: any = res;
        this.listRole = d.data;
      },
      err => {
        this.toastService.openErrorToast(err.error.msgCode)
      }
    )
  }

  closeModal() {
    this.activeModal.dismiss('Cross click');
    this.activeModal.dismiss(true);
  }

  onSubmitData() {
    this.setValueToField('permissionList', this.getPermissionForFormSubmid());
    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      return;
    }
    console.warn(91, "Data submit", this.form.value);
    const apiCall = this.type === 'add' ? this.groupPermissionService.save(this.form.value) : this.groupPermissionService.update(this.form.value);
    apiCall.subscribe(
      res => {
        this.eventManager.broadcast({
          name: 'groupPermissionListChange'
        });
        this.activeModal.dismiss(true);
        this.type === 'add'
          ? this.toastService.openSuccessToast("Thêm thông tin thành công")
          : this.toastService.openSuccessToast("Cập nhật thông tin thành công");
      },
      err => {
        let msg;
        if (err.error.code === 'EGP010') {
          msg = 'Nhóm quyền đã tồn tại, không thể thêm';
        } else if (err.error.code === 'EGP003') {
          msg = 'Lỗi hệ thống, nhóm quyền chưa được lưu';
        } else if (err.error.code === 'BK000') {
          msg = 'Lỗi valid dữ liệu';
        } else {
          msg = 'Lỗi hệ thống, danh sách quyền chưa được lưu';
        }
        this.toastService.openErrorToast(
          msg,
          this.translateService.instant('common.toastr.title.error')
        );
      }
    );
  }

  getPermissionForFormSubmid(): any[] {
    return this.checkedTreeView.map(e => {
      return {permissionId: "" + e}
    });
  }

  customSearchFn(term: string, item: any) {
    const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    const newRegEx = new RegExp(replacedKey, 'gi');
    const purgedPosition = item.name.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    return newRegEx.test(purgedPosition);
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  onClearPosition() {
    this.setValueToField('status', 'ACTIVE');
  }

  getValueOfField(item) {
    return this.form.get(item).value;
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }

  onResize() {
    this.height = this.heightService.onResize();
  }

  trimSpace(element) {
    const value = this.getValueOfField(element);
    if (value) {
      this.setValueToField(element, value.trim());
    }
  }

  onCancel() {
    if (this.type === 'update') {
      if (
        this.form.value.id === this.data.id &&
        this.form.value.code === this.data.code &&
        this.form.value.name === this.data.name &&
        this.form.value.status === this.data.status &&
        this.form.value.note === this.data.note &&
        this.form.value.permissionList === this.data.permissionList
      ) {
        this.closeModal();
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.closeModal();
          }
        });
      }
    }
    if (this.type === 'add') {
      if (
        this.form.value.id === '' &&
        this.form.value.code === '' &&
        this.form.value.name === '' &&
        this.form.value.status === 'ACTIVE' &&
        this.form.value.note === '' &&
        this.form.value.permissionList === null
      ) {
        this.closeModal();
      } else {
        const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
        modalRef.componentInstance.type = 'confirm';
        modalRef.componentInstance.onCloseModal.subscribe(value => {
          if (value === true) {
            this.closeModal();
          }
        });
      }
    }
  }

  getAllPermission() {
    const id = this.data ? this.data.id : null;
    this.groupPermissionService
      .findAllPermission({id: id})
      .subscribe(
        res => {
          const d: any = res;
          const objTree: any = d.data;
          for (let i = 0; i < objTree.length; i++) {
            this.items.push(new TreeviewItem(objTree[i]));
          }
          this.getChecked(this.items, this.checkIdOld);
        },
        err => {
        }
      )
  }

  getChecked(treeView: TreeviewItem[], result: any[]) {
    treeView.forEach(tree => {

      if (tree.checked) {
        result.push(tree.value);
      } else {
        if (tree.indeterminate) {
          result.push(tree.value);
        }
      }
      if (tree.children) {
        this.getChecked(tree.children, result);
      }
    });
    this.checkedTreeView = this.checkIdOld;
  }

  onSelectedChange(env) {
    this.checkIdOld = [];
    this.getChecked(this.items, this.checkIdOld);
  }

  private buildForm() {
    this.form = this.formBuilder.group({
      id: '',
      code: ['', Validators.required],
      name: ['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      status: ['ACTIVE'],
      note: ['', Validators.maxLength(500)],
      permissionList: [[]]
    });
    if (this.data) {
      this.setDataDefault();
    }
  }

}
