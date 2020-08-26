import {Component, HostListener, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HeightService} from 'app/shared/services/height.service';
import {SHOW_HIDE_COL_HEIGHT} from 'app/shared/constants/perfect-scroll-height.constants';
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from 'app/shared/constants/pagination.constants';
import {ConfirmModalComponent} from 'app/shared/components/confirm-modal/confirm-modal.component';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {OrganizationCategoriesModel} from 'app/core/models/system-categories/organization-categories.model';
import {CommonService} from 'app/shared/services/common.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {ToastService} from 'app/shared/services/toast.service';
import {TranslateService} from '@ngx-translate/core';
import {Observable, forkJoin, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ListHandoverProductService} from 'app/core/services/list-handover-product/list-handover-product.service'
import {ListHandoverProduct} from "app/core/models/list-handover-product/list-handover-product.model";
import {FormStoringService} from "app/shared/services/form-storing.service";

@Component({
  selector: 'jhi-list-handover-product',
  templateUrl: './list-handover-product.component.html',
  styleUrls: ['./list-handover-product.component.scss'],
})
export class ListHandoverProductComponent implements OnInit {
  isCheckedAdd = false;
  height: number;
  SHOW_HIDE_COL_HEIGHT = SHOW_HIDE_COL_HEIGHT;
  itemsPerPage: any;
  maxSizePage: any;
  routeData: any;
  page: any;
  second: any;
  totalItems: any;
  previousPage: any;
  predicate: any;
  reverse: any;
  checkCode = false;
  parentOrganizationList = new Observable<any[]>();
  groupOrganizationList: any[] = [];
  listUnit$ = new Observable<any[]>();
  unitSearch;
  debouncer: Subject<string> = new Subject<string>();
  listUnit1$ = new Observable<any[]>();
  unitSearch1;
  debouncer1: Subject<string> = new Subject<string>();
  isModalConfirmShow = false;
  action: String;
  fieldArray: ListHandoverProduct[] = [];
  statusOptions = [{statusId: 0, statusValue: "Chưa làm"},
    {statusId: 1, statusValue: "Đang làm"},
    {statusId: 2, statusValue: "Đã hoàn thành"},
    {statusId: 3, statusValue: "Đã bàn giao khách hàng"},
    {statusId: 4, statusValue: "Đang sửa theo comment khách hàng"}
  ];
  listDelete = [];
  isEdit = [];
  objClone: ListHandoverProduct[] = [];
  disableAddNew = false;
  isFieldArrayAddNew = false;
  projectId: any;
  originalSize = 0;

  constructor(
    private http: HttpClient,
    private activatedRoute: ActivatedRoute,
    private heightService: HeightService,
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private commonService: CommonService,
    private toastService: ToastService,
    private translateService: TranslateService,
    // public activeModal: NgbActiveModal,
    private listHandoverProductService: ListHandoverProductService,
    protected router: Router,
    private storageService: FormStoringService
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

  permissionCheck(permission?: string) {
    return this.commonService.havePermission(permission);
  }

  buildForm() {
    this.activatedRoute.queryParams.subscribe(param => {
      this.projectId = param.id;
      this.listHandoverProductService.getProductList(param.id).subscribe(
        res => {
          if (res) {
            if (res.length === 0) {
              this.isFieldArrayAddNew = true;
            }
            this.originalSize = res.length;
            for (let i = 0; i < res.length; i++) {
              this.isEdit[i] = true;
              this.fieldArray.push(res[i]);
              this.objClone[i] = JSON.parse(JSON.stringify(res[i]));
            }
          } else {
            this.fieldArray = [];
          }
        },
        err => {
          this.fieldArray = [];
        }
      );
    });
  }

  ngOnInit() {
    this.buildForm();
    this.onResize();
  }

  addFieldValue() {
    const newRecord = {
      projectId: this.projectId,
      productName: '',
      statusHandover: 0,
      description: '',
      createBy: this.storageService.get('user').humanResourceId
    };
    this.disableAddNew = true;
    this.isEdit.push(false);
    this.fieldArray.push(newRecord);
  }

  updateRow($event: MouseEvent, i: number) {
    this.isEdit[i] = !this.isEdit[i];
  }

  onNotUpdate(index) {
    if (this.objClone[index] != null) {
      this.fieldArray[index] = this.objClone[index];
      this.isEdit[index] = !this.isEdit[index];
    } else {
      this.fieldArray.splice(index, 1);
    }
    this.disableAddNew = false;
  }

  onUpdate(index) {
    if (this.validData()) {
      this.isEdit[index] = !this.isEdit[index];
      if (index === this.fieldArray.length - 1) {
        this.disableAddNew = false;
      }
    }
  }

  deleteFieldValue(index) {
    this.isModalConfirmShow = true;
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'delete';
    modalRef.componentInstance.param = this.fieldArray[index].productName;
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.listDelete.push(this.fieldArray[index].productHandoverId);
        this.fieldArray.splice(index, 1);
        this.isEdit.splice(index, 1);
      }
      this.isModalConfirmShow = false;
    });
  }

  equals(obj1, obj2) {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }

  onCloseAddModal() {
    if (!this.equals(this.fieldArray, this.objClone) && (this.fieldArray[this.fieldArray.length - 1].productName !== '' || this.fieldArray[this.fieldArray.length - 1].description !== '' || this.fieldArray[this.fieldArray.length - 1].statusHandover !== 0)) {

      this.isModalConfirmShow = true;
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'confirm';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.router.navigate(['system-categories/project-management']);
        }
        this.isModalConfirmShow = false;
      });
    } else {
      this.router.navigate(['system-categories/project-management']);
    }

  }

  onSubmitData() {
    if (this.validData()) {
      this.isModalConfirmShow = true;
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'confirmModal';
      modalRef.componentInstance.message = 'Bạn có chắc chắn muốn ghi lại sản phẩm?';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          // this.activeModal.dismiss();
          const saveReq = this.listHandoverProductService.save(this.fieldArray);
          const deleteReq = this.listHandoverProductService.delete(this.listDelete);
          forkJoin([saveReq, deleteReq]).subscribe(res => {
            if (res[0].status === 200 && res[1].status === 200) {
              if (this.isFieldArrayAddNew === true) {
                this.toastService.openSuccessToast('Thêm danh sách sản phẩm bàn giao thành công!');
              } else if (this.fieldArray.length === 0) {
                this.toastService.openSuccessToast('Xóa danh sách sản phẩm bàn giao thành công!');
              } else {
                this.toastService.openSuccessToast('Sửa danh sách sản phẩm bàn giao thành công!');
              }
            }
          }, error1 => {
            this.toastService.openErrorToast('Lỗi hệ thống!');
          });

          this.router.navigate(['system-categories/project-management']);
        }
        this.isModalConfirmShow = false;
      });
    }
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  validData() {
    let checkValid = true;
    for (let i = 0; i < this.fieldArray.length; i++) {
      if (this.fieldArray[i].productName.length < 1) {
        this.toastService.openErrorToast('Trường Sản phẩm tại dòng ' + (i + 1) + ' không được để trống!');
        checkValid = false;
      } else if (this.fieldArray[i].productName.length > 255) {
        this.toastService.openErrorToast('Trường Sản phẩm tại dòng ' + (i + 1) + ' không được dài quá 255 kí tự!');
        checkValid = false;
      } else if (this.fieldArray[i].description.length > 1000) {
        this.toastService.openErrorToast('Trường Ghi chú tại dòng ' + (i + 1) + ' không được dài quá 1000 kí tự!');
        checkValid = false;
      } else {
        this.fieldArray[i].updateBy = this.storageService.get('user').humanResourceId;
        this.fieldArray[i].productName = this.fieldArray[i].productName.trim();
        this.fieldArray[i].description = this.fieldArray[i].description.trim();
      }
    }
    return checkValid;
  }

  export() {
    this.listHandoverProductService.exportData(this.projectId).subscribe(res => {
      if (res.status === 200) {
        this.downloadFile(res);
        this.toastService.openSuccessToast('Export thành công!');
      }
    }, error1 => {
      this.toastService.openErrorToast('Export gặp lỗi hệ thống!');
    });
  }

  downloadFile(data) {
    if (!data || !data.body) {
      this.toastService.openWarningToast(this.translateService.instant('common.toastr.messages.info.noDataToExport'));
      return;
    }
    const fileName = data.headers.get('File');
    const link = document.createElement('a');
    const url = URL.createObjectURL(data.body);
    link.setAttribute('href', url);
    link.setAttribute('download', fileName);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
