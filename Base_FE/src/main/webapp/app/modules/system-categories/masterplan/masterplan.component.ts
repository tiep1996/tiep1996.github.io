import {Component, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgxSpinnerService} from 'ngx-spinner';
import {ToastService} from 'app/shared/services/toast.service';
import {ConfirmModalComponent} from 'app/shared/components/confirm-modal/confirm-modal.component';
import {MasterPlanModel} from 'app/core/models/masterplan/masterplan.model';
import {ProjectPlanModel} from 'app/core/models/masterplan/projectplan.model';
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from 'app/shared/constants/pagination.constants';
import {HeightService} from 'app/shared/services/height.service';
import {CommonApiService} from 'app/core/services/common-api/common-api.service';
import {MasterplanServiceService} from 'app/core/services/masterplan/masterplan-service.service';
import {forkJoin, Observable} from 'rxjs';
import {ModalConfirmComponent} from 'app/modules/system-categories/masterplan/modal-confirm/modal-confirm.component';
import {CommonService} from "app/shared/services/common.service";
import {FormStoringService} from "app/shared/services/form-storing.service";

// import { ExportService } from './_services/export.service';


@Component({
  selector: 'jhi-masterplan',
  templateUrl: './masterplan.component.html',
  styleUrls: ['./masterplan.component.scss']
})
export class MasterplanComponent implements OnInit {

  form: FormGroup;
  totalItems: any;
  listId: any[];
  list: Object = null;
  masterPlanModel: MasterPlanModel;
  projectPlanModel: ProjectPlanModel;
  // check thoi gian bat dau < thoi gian ket thuc
  error;
  message;
  buttonDisabled = false;
  startTime: number;
  endTime: number;
  signatureStatus: string;
  contractStatu: string;
  partnerId: string;
  listUnit$ = new Observable<any[]>();
  data;
  searchData: any;

  height: number;
  type: string;
  selectedData;
  itemsPerPage: any;
  maxSizePage: any;
  routeData: any;
  page: any;
  second: any;
  previousPage: any;
  predicate: any;
  reverse: any;
  statusList: any[] = [];
  invoiceTemplateList: any[] = [];
  newDate: any;
  buttonDisable;
  lstProjectPlan: any[] = [];
  lstDelete: any[] = [];
  isEdit = [];
  objClone: ProjectPlanModel[] = [];
  isSuccess = true;
  isModalConfirmShow = false;
  lstHumanResource: any[] = [];
  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  btnSaveDisable = false;
  checkLstProjectPlan: any[] = [];
  checkMasterPlan : MasterPlanModel;
  role: any;
  createBy: any;
  change = false;

  SigningStatus: any;
  contractStatus: any;
  SHOW_HIDE_COL_HEIGHT;
  columns = [
    {key: 0, value: this.translateService.instant('contractManagement.idMaDoiTac'), isShow: true},
    {key: 1, value: this.translateService.instant('contractManagement.TTK'), isShow: true},
    {key: 2, value: this.translateService.instant('contractManagement.SHDTN'), isShow: true},
    {key: 3, value: this.translateService.instant('contractManagement.TTHD'), isShow: true},
    {key: 4, value: this.translateService.instant('contractManagement.TTTD'), isShow: true},
    {key: 5, value: this.translateService.instant('contractManagement.TMMTN'), isShow: true},
    {key: 6, value: this.translateService.instant('contractManagement.MMDTT'), isShow: true},
  ];
  //nuctv 10/08
  user = JSON.parse(localStorage.getItem('user'));
  position: any = [];

  //end nuctv
  constructor(
    private modalService: NgbModal,
    private translateService: TranslateService,
    private spinner: NgxSpinnerService,
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private heightService: HeightService,
    private toastService: ToastService,
    private commonApiService: CommonApiService,
    private masterplanServiceService: MasterplanServiceService,
    private router: Router,
    private commonService: CommonService,
    private storageService: FormStoringService
    // private exportService : ExportService,
  ) {
    // this.totalItems =;
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

  onClickAddEffort() {
    const obj: any = {
      module: '',
      mileStone: '',
      humanResourceId: '',
      startDate: '',
      endDate: '',
      deadline: '',
      description: ''
    };
    this.isEdit.unshift(false);
    this.lstProjectPlan.unshift(obj);
    this.buttonDisabled = true;
    this.change = false;
  }


  ngOnInit() {
    this.searchData = {};
    this.startTime = new Date().setDate(1);
    this.endTime = new Date().setDate(1);
    this.buidForm();
    // this.getAllInvoiceTemplate();
    this.onResize();
    this.activatedRoute.queryParams.subscribe(param => {
      this.loadAll(param.id);
      //nuctv;
      this.masterplanServiceService.findListMember(param.id, this.user.humanResourceId).subscribe(res => {
        if (res.length !== 0) {
          res.forEach(value => {
            this.position.push(value.role);
          });
        }
      });
    })
  }

  permissionCheck(permission?: string) {
    return this.commonService.havePermission(permission);
  }

  check() {
    const role = this.storageService.get('user').role;
    if (this.createBy != null && role !== "QA Manager") {
      return true;
    } else {
      if (role === "BA Manager" && this.role === 1) {
        return true;
      } else if (role === "PM/TeamLead" && this.role === 0) {
        return true;
      }
      return false;
    }
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  toggleColumns(col) {
    col.isShow = !col.isShow;
  }

  // check validate trạng thái hợp đồng trước khi xóa
  checkValidateSignContract(data: any) {
    const signContract = Number(data.contractStatus);
    if (signContract === 0) {
      return false;
    } else {
      return true;
    }
  }

  //
  convertDate(str) {
    if (str === null || str === '') {
      return "";
    } else {
      const date = new Date(str),
        mnth = ('0' + (date.getMonth() + 1)).slice(-2),
        day = ('0' + date.getDate()).slice(-2);
      return [date.getFullYear(), mnth, day].join('-');
    }
  }

  onChangeDate(flag: any) {
    this.checkChange();
    // convertDate(any);
  }

  getDate(value) {
    return new Date(value).getTime();
  }

  loadAll(id) {
    const projectId: number = +id;
    const getProjectPlan = this.masterplanServiceService.getAllByProjectPlanId(projectId);
    const getHumanResource = this.masterplanServiceService.getHumanResource(projectId);
    forkJoin([getProjectPlan, getHumanResource]).subscribe(res => {
      this.masterPlanModel = res[0].body;
      this.checkMasterPlan = JSON.parse(JSON.stringify(res[0].body));
      this.lstHumanResource = res[1].body;
      this.role = res[0].body.isBA;
      this.createBy = res[0].body.createBy;
      for (let i = 0; i < res[0].body.lstprojectPlanDTO.length; i++) {
        this.isEdit[i] = true;
        this.lstProjectPlan.push(res[0].body.lstprojectPlanDTO[i]);
        this.checkLstProjectPlan.push(JSON.parse(JSON.stringify(res[0].body.lstprojectPlanDTO[i])));
      }

    })
  }

  onSave() {
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'confirmModal';
    modalRef.componentInstance.message = 'Bạn có muốn ghi lại kế hoạch dự án không?';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.masterPlanModel.lstprojectPlanDTO = this.lstProjectPlan;
        const data = this.masterPlanModel;
        data.dateDeliveryKBKT = this.convertDate(data.dateDeliveryKBKT);
        data.actualDateDelivery = this.convertDate(data.actualDateDelivery);
        data.dateSendingPlan = this.convertDate(data.dateSendingPlan);
        data.endDate = this.convertDate(data.endDate);
        data.dateDemo = this.convertDate(data.dateDemo);
        data.humanId = this.user.humanResourceId;
        const saveReq = this.masterplanServiceService.edit(data);
        const deleteReq = this.masterplanServiceService.deleteById(this.lstDelete);
        forkJoin([saveReq, deleteReq]).subscribe();
        this.toastService.openSuccessToast('Thêm mới/Sửa kế hoạch dự án thành công');
        this.router.navigate(['/system-categories/project-management']);
      }
    })
  }

  checkChange(){
    if(this.masterPlanModel.actualDateDelivery !== this.checkMasterPlan.actualDateDelivery || this.masterPlanModel.dateDemo !== this.checkMasterPlan.dateDemo
      || this.masterPlanModel.dateDeliveryKBKT !== this.checkMasterPlan.dateDeliveryKBKT || this.masterPlanModel.dateSendingPlan !== this.checkMasterPlan.dateSendingPlan||
      this.masterPlanModel.endDate !== this.checkMasterPlan.endDate){
      this.change = true;
    }
    if (this.lstProjectPlan.length === this.checkLstProjectPlan.length) {
      for (let i = 0; i < this.lstProjectPlan.length; i++) {
        if (this.lstProjectPlan[i].mileStone !== this.checkLstProjectPlan[i].mileStone || this.lstProjectPlan[i].module !== this.checkLstProjectPlan[i].module
          || this.lstProjectPlan[i].humanResourceId !== this.checkLstProjectPlan[i].humanResourceId || this.lstProjectPlan[i].startDate !== this.checkLstProjectPlan[i].startDate
          || this.lstProjectPlan[i].endDate !== this.checkLstProjectPlan[i].endDate || this.lstProjectPlan[i].deadline !== this.checkLstProjectPlan[i].deadline
          || this.lstProjectPlan[i].description !== this.checkLstProjectPlan[i].description) {
          this.change = true;
        }
      }
    } else {
      this.change = true;
    }
  }

  onBack() {
    this.checkChange();
    if(this.change===true){
      const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
      modalRef.componentInstance.type = 'confirmModal';
      modalRef.componentInstance.message = 'Bạn có muốn hủy bỏ kế hoạch không?';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.router.navigate(['/system-categories/project-management']);
        }
      })
    } else {this.router.navigate(['/system-categories/project-management']);}

  }

  onBackView() {
    this.router.navigate(['/system-categories/project-management']);
  }

  onEdit(i) {
    this.objClone[i] = JSON.parse(JSON.stringify(this.lstProjectPlan[i]));
    this.isEdit[i] = !this.isEdit[i];
    this.buttonDisabled = true;
    this.change = false;
  }

  onUpdate(index) {
    // this.lstProjectPlan[index].clicked = true;
    if (this.validDate(index)) {
      this.isEdit[index] = !this.isEdit[index];
      this.buttonDisabled = false;
      this.checkChange();
      this.objClone.splice(0,1);
    }
    this.checkChange()
  }

  onNotUpdate(index) {
    if (this.objClone[index] != null) {
      this.lstProjectPlan[index] = this.objClone[index];
      this.isEdit[index] = !this.isEdit[index];
    } else {
      this.lstProjectPlan.splice(index, 1)
      this.isEdit.splice(index, 1)
    }
    this.buttonDisabled = false;
    this.checkChange();

  }

  changeDecision() {
    this.isSuccess = !this.isSuccess;
  }

  onDelete(i) {
    this.isModalConfirmShow = true;
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'deleteMultiple';
    modalRef.componentInstance.message = 'Bạn có chắc chắn muốn xóa thông tin kế hoạch này không?';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.lstDelete.push(this.lstProjectPlan[i].projectPlanId);
        this.lstProjectPlan.splice(i, 1);
        this.isEdit.splice(i, 1);
        this.change=false;
        this.checkChange();
      }
    });
  }

  validDate(i) {
    let checkValid = true;
    if (this.lstProjectPlan[i].module === "") {
      this.toastService.openErrorToast('Trường Module không được để trống.');
      return checkValid = false;
    }
    if (this.lstProjectPlan[i].mileStone === "") {
      this.toastService.openErrorToast('Trường MileStones không được để trống.');
      return checkValid = false;
    }
    if (this.lstProjectPlan[i].humanResourceId === "" || this.lstProjectPlan[i].humanResourceId === null) {
      this.toastService.openErrorToast('Trường người thực hiện không được để trống.');
      return checkValid = false;
    }
    if (this.lstProjectPlan[i].startDate === "") {
      this.toastService.openErrorToast('Trường Ngày bắt đầu không được để trống.');
      return checkValid = false;
    }
    if (this.lstProjectPlan[i].deadline === "") {
      this.toastService.openErrorToast('Trường thời hạn không được để trống.');
      return checkValid = false;
    }
    if (this.lstProjectPlan[i].startDate > this.lstProjectPlan[i].deadline) {
      this.toastService.openErrorToast('Thời hạn phải lớn hơn hoặc bằng ngày bắt đầu');
      return checkValid = false;
    }
    if (this.lstProjectPlan[i].endDate > this.lstProjectPlan[i].deadline) {
      this.toastService.openErrorToast('Thời hạn phải lớn hơn hoặc bằng ngày kết thúc');
      return checkValid = false;
    }
    if (this.lstProjectPlan[i].endDate === "") {
      this.lstProjectPlan[i].endDate = this.lstProjectPlan[i].deadline;
    } else if (this.lstProjectPlan[i].startDate > this.lstProjectPlan[i].endDate) {
      this.toastService.openErrorToast('Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu');
      return checkValid = false;
    }

    return checkValid;
  }

  openModal(type?: number) {
    if (this.lstProjectPlan.length !== 0) {
      if (type === 1) {
        const modalRef = this.modalService.open(ModalConfirmComponent, {
          size: 'lg',
          backdrop: 'static',
          keyboard: false,
          windowClass: 'myCustomModalClass'
        });
        modalRef.componentInstance.model = this.masterPlanModel;
        modalRef.componentInstance.type = type;
        modalRef.componentInstance.recorded.subscribe(value => {
          // this.masterPlanModel.statusBA = value.status;
        })
      }
      if (type === 2) {
        const modalRef = this.modalService.open(ModalConfirmComponent, {
          size: 'lg',
          backdrop: 'static',
          keyboard: false,
          windowClass: 'myCustomModalClass'
        });
        modalRef.componentInstance.model = this.masterPlanModel;
        modalRef.componentInstance.type = type;
        modalRef.componentInstance.recorded.subscribe(value => {
          // this.masterPlanModel.statusTest = value.status;
          // this.masterPlanModel.noteTest = value.note;
          // this.masterPlanModel.reasonTest = value.reason;
        })
      }
      if (type === 3) {
        const modalRef = this.modalService.open(ModalConfirmComponent, {
          size: 'lg',
          backdrop: 'static',
          keyboard: false,
          windowClass: 'myCustomModalClass'
        });
        modalRef.componentInstance.model = this.masterPlanModel;
        modalRef.componentInstance.type = type;
        modalRef.componentInstance.recorded.subscribe(value => {
          // this.masterPlanModel.statusPM = value.status;
          // this.masterPlanModel.notePM = value.note;
          // this.masterPlanModel.reasonPM = value.reason;
        })
      }
      if (type === 4) {
        if (this.masterPlanModel.statusBA === 1 && this.masterPlanModel.statusPM === 1 && this.masterPlanModel.statusTest === 1) {
          const modalRef = this.modalService.open(ModalConfirmComponent, {
            size: 'lg',
            backdrop: 'static',
            keyboard: false,
            windowClass: 'myCustomModalClass'
          });
          modalRef.componentInstance.model = this.masterPlanModel;
          modalRef.componentInstance.type = type;
          modalRef.componentInstance.recorded.subscribe(value => {
            // this.masterPlanModel.statusQA = value.status;
            // this.masterPlanModel.noteQA = value.note;
            // this.masterPlanModel.reasonQA = value.reason;
          })
        } else {
          this.toastService.openWarningToast("Cần BA Manager, Test Leader và PM/TeamLead xác nhận!")
        }
      }
    } else {
      this.toastService.openWarningToast("Chưa có kế hoạch dự án được lập")
    }
  }

  export() {
    this.masterplanServiceService.exportData(this.masterPlanModel.projectId).subscribe(res => {
      if (res.status === 200) {
        this.downloadFile(res);
        this.toastService.openSuccessToast('Export thành công!');
      }
    }, error1 => {
      this.toastService.openSuccessToast('Export gặp lỗi hệ thống!');
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
    link.setAttribute('down', fileName);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      actualDateDelivery: '',
      endDate: '',
      dateSendingPlan: [''],
      dateDemo: [''],
      projectId: [''],
      name: [''],
      dateDeliveryKBKT: ['']
    });
  }

}
