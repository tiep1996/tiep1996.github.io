import {Component, EventEmitter, OnInit, Output} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HeightService} from 'app/shared/services/height.service';
import {HumanResourcesApiService} from 'app/core/services/Human-resources-api/human-resources-api.service';
import {Observable, Subject, Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {REGEX_PATTERN} from 'app/shared/constants/pattern.constants';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgxSpinnerService} from 'ngx-spinner';
import {JhiEventManager} from 'ng-jhipster';
import {ToastService} from 'app/shared/services/toast.service';
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from 'app/shared/constants/pagination.constants';
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {AddHumanResourcesComponent} from "app/modules/system-categories/human-resources/add-human-resources/add-human-resources.component";
import {APP_PARAMS_CONFIG} from 'app/shared/constants/app-params.constants';
import {HttpResponse} from '@angular/common/http';


@Component({
  selector: 'jhi-human-resources',
  templateUrl: './human-resources.component.html',
  styleUrls: ['./human-resources.component.scss']
})
export class HumanResourcesComponent implements OnInit {
  form: FormGroup;
  height: number;
  itemsPerPage: any;
  maxSizePage: any;
  routeData: any;
  page: number;
  second: any;
  totalItems: any;
  previousPage: any;
  predicate: any;
  reverse: any;
  userList: any;
  formValue;
  eventSubscriber: Subscription;
  listUnit$ = new Observable<any[]>();
  unitSearch;
  debouncer: Subject<string> = new Subject<string>();
  positionList: any[] = [];
  cities = [
    {id: 1, name: 'Hoạt động'},
    {id: 3, name: 'Không hoạt động'},

  ];
  centerList: any[] = [];
  active = 1;
  user = JSON.parse(localStorage.getItem('user'));
  constructor(
    private heightService: HeightService,
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private humanResourcesApiService: HumanResourcesApiService,
    // private translateService: TranslateService,
    private modalService: NgbModal,
    protected router: Router,
    private spinner: NgxSpinnerService,
    private eventManager: JhiEventManager,
    private toastService: ToastService,
    // private sysUserService: SysUserService
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
    this.onResize();
    this.loadAll();
    this.registerChange();
    this.getCenterList();
    this.getPositionList();
  }

  // }
  loadAll() {
    this.spinner.show();
    this.humanResourcesApiService
      .searchHumanResources({
        page: this.page,
        pageSize: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
        username: this.form.get('code').value ? this.form.get('code').value : '',
        parcode: this.form.get('positionName').value ? this.form.get('positionName').value : '',
        centerId: this.form.get('centerId').value ? this.form.get('centerId').value : '',
        // active: this.active,
        active: this.form.get('active').value ? this.form.get('active').value : '',
      })
      .subscribe(
        res => {
          this.spinner.hide();
          this.paginateUserList(res);
        },
        err => {
          this.spinner.hide();
          // this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.load'));
          this.toastService.openErrorToast("loi");
        }
      );
  }

  getCenterList() {
    this.humanResourcesApiService.getCenterList().subscribe(
      res => {
        if (res) {
          this.centerList = res.data;
        } else {
          this.centerList = [];
        }
      },
      error => {
        this.centerList = [];
      }
    );
  }


  get formControl() {
    return this.form.controls;
  }

  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('HumanResourcesChange', response => this.loadAll());
  }

  setValueOfForm(formValue) {
    this.formValue = formValue;
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

  onSearchData() {
    this.transition();
  }

  transition() {
    this.router.navigate(['/system-categories/human-resources'], {
      queryParams: {
        page: this.page,
        pageSize: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
        code: this.form.get('code').value ? this.form.get('code').value : '',
        parcode: this.form.get('positionName').value ? this.form.get('positionName').value : '',
        active: this.active,
        name:"sdad",
      }
    });
    this.loadAll();
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

  customSearchFn(term: string, item: any) {
    const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    const newRegEx = new RegExp(replacedKey, 'gi');
    const purgedPosition = item.code.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    return newRegEx.test(purgedPosition);
  }

  onChangePosition(event) {
    if (event) {
      this.setValueToField('positionName', event.code);
      this.setValueToField('positionId', event.id);
    }
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  onClearPosition() {
    this.setValueToField('positionName', null);
    this.setValueToField('positionId', null);
  }
  onChangePosition1(event) {
    this.active = event.id;
  }


  onDelete(data) {

    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    if (data.active === 1) {
      modalRef.componentInstance.type = 'deactivate';
      modalRef.componentInstance.param = 'nhân sự';
    } else if (data.active === 3){
      modalRef.componentInstance.type = 'active';
      modalRef.componentInstance.param = ' nhân sự';
    }
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.onDelete1(data.humanResourceId);
      }
    });
  }

  onResetPassword(data) {
    // if(data.statusOverview !==14){
    //   this.toastService.openErrorToast('Trạng thái dự án khác Chưa bắt đầu khảo sát, không được phép xóa!');
    // }else{
    const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    modalRef.componentInstance.type = 'reset';
    modalRef.componentInstance.param = 'mật khẩu';
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        this.resetpassword(data);
      }
    });
    // }

  }


  onDelete1(ids) {
    this.humanResourcesApiService.deleteHumanResources(ids).subscribe(res => {
      this.spinner.show();
      if (res.data) {
        this.spinner.hide();
        if(res.data.code === "BK0010"){
          this.toastService.openSuccessToast("Mở khóa nhân sự thành công!");
        }else if(res.data.code === "BK009"){

          this.toastService.openSuccessToast("Khóa nhân sự thành công!");
        }
        this.loadAll();
      }
    }, error => {
      this.spinner.hide();
      // this.toastService.openErrorToast(this.translateService.instant('user.invalidDelete'));
      this.toastService.openErrorToast("Khóa nhân sự thất bại, có rằng buộc dữ liệu");
    });
  }

// resetpassword
  resetpassword(item) {
    this.spinner.show();
    this.humanResourcesApiService.resetpassword(item.humanResourceId).subscribe(res => {
      if (res.data) {
        this.toastService.openSuccessToast('Mật khẩu mới đã được gửi tới email '+item.email+", xin vui lòng check email!");
        this.spinner.hide();
      } else {
        this.spinner.hide();
        // this.toastService.openErrorToast(this.translateService.instant('user.invalidDelete'));
        this.toastService.openErrorToast('user.invalidDelete');
      }
    });
  }


  private buidForm() {
    this.form = this.formBuilder.group({
      centerId: [],
      active:[1],
      code: [''],
      positionName: [null],

    });
  }

  private paginateUserList(res) {
    console.warn(1234, res);
    this.totalItems = res.dataCount;
    this.userList = res.data;
  }

  getPositionList() {
    this.humanResourcesApiService
      .getDepartment({
        type: "DEPART"
      })
      .subscribe(
        res => {
          if (res) {
            this.positionList = res;
          } else {
            this.positionList = [];
          }
        },
        err => {
          this.positionList = [];
        }
      );
  }

  getValueOfField(item) {
    return this.form.get(item).value;
  }

  trimSpace(element) {
    const value = this.getValueOfField(element);
    if (value) {
      this.setValueToField(element, value.trim());
    }
  }

  onClick() {
    const modalRef = this.modalService.open(AddHumanResourcesComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false,
      windowClass: 'myCustomModalClass'
    });
    modalRef.componentInstance.type = 'add';
    modalRef.componentInstance.data = {};
    modalRef.componentInstance.onCloseModal.subscribe(value => {
    });
  }

  /*duc*/
  openModalAddUser(type?: string, selectedData?: any) {
    const modalRef = this.modalService.open(AddHumanResourcesComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.type = type;
    modalRef.componentInstance.id = selectedData ? selectedData.humanResourceId : null;
    modalRef.result.then(result => {
      if(result) {
        this.loadAll();
      }
    }).catch(() => {
      this.loadAll();
    });

  }


  /* end duc*/

  // đong bo nhan su
  insertUDB() {
    this.spinner.show();
    this.humanResourcesApiService.synchronizedUser(this.user.humanResourceId).subscribe(
      res=>{
        this.toastService.openSuccessToast("Thêm mới: "+res.total_add+"\n"+"Cập nhật: "+res.total_update,"Đồng bộ thành công");
        this.spinner.hide();
        this.loadAll();
      },error => {
        this.toastService.openErrorToast("Đồng bộ thất bại");
        this.spinner.hide();
        this.loadAll();
      }
    );
  }
}
