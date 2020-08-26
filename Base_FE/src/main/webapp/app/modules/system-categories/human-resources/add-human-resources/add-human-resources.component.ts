import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {debounceTime} from "rxjs/operators";
import {TIME_OUT} from "app/shared/constants/set-timeout.constants";
import {Observable, of, Subject, Subscription} from "rxjs";
import {ITEMS_PER_PAGE, MAX_SIZE_PAGE} from "app/shared/constants/pagination.constants";
import {HttpResponse} from "@angular/common/http";
import {STATUS_CODE} from "app/shared/constants/status-code.constants";
// import {HumanResourceService} from "app/core/services/human-resource-api/human-resource.service";
import {REGEX_PATTERN} from "app/shared/constants/pattern.constants";
import {APP_PARAMS_CONFIG} from "app/shared/constants/app-params.constants";
import {NgbActiveModal, NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {SysUserModel} from "app/core/models/system-categories/sys-user.model";
import {CommonService} from "app/shared/services/common.service";
import {ToastService} from "app/shared/services/toast.service";
import {NgxSpinnerService} from "ngx-spinner";
import {JhiEventManager} from "ng-jhipster";
import {HeightService} from "app/shared/services/height.service";
import {SysUserService} from "app/core/services/system-management/sys-user.service";
import {TranslateService} from "@ngx-translate/core";
import {ConfirmModalComponent} from "app/shared/components/confirm-modal/confirm-modal.component";
import {ActivatedRoute, Router} from "@angular/router";
import {HumanResourcesApiService} from "app/core/services/Human-resources-api/human-resources-api.service";
import {HumanResouces} from "app/core/models/human-resources/human-resouces.model";
import {DatePipe} from "@angular/common";


@Component({
  selector: 'jhi-add-human-resources',
  templateUrl: './add-human-resources.component.html',
  styleUrls: ['./add-human-resources.component.scss'],
  providers: [DatePipe]
})
export class AddHumanResourcesComponent implements OnInit {
  @Input() type;
  @Input() id:any;
  @Output() passEntry:EventEmitter<any> = new EventEmitter()
  ngbModalRef: NgbModalRef;
  form: FormGroup;
  listUnit$ = new Observable<any[]>();
  unitSearch;
  debouncer: Subject<string> = new Subject<string>();
  departmentList: any[] = [];
  centerList : any[] =[];
  listUserGroup: any[];
  positionList:any[] = [];
  isDuplicateEmail = false;
  isDuplicateUsername= false;
  height: number;
  name: string;
  userDetail: any;
  statusList = [{
    id: 1,
    name: 'Hoạt động'
  }, {
    id: 3,
    name: 'Không hoạt động'
  }];

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
    private humanResourceService: HumanResourcesApiService,
    private translateService: TranslateService,
    private datepipe: DatePipe
  ) {
    this.height = this.heightService.onResize();
  }

  ngOnInit() {

    this.buildForm();
    this.debounceOnSearch();
    this.getDeparmentList();
    this.getPositionList();
    // this.getGroupUsers();
    this.getCenterList();

  }

  private buildForm() {
    this.form = this.formBuilder.group({
      humanResourceId: null,
      username: ['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      firstName: ['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      email: ['', Validators.compose([Validators.required, Validators.maxLength(50)])],
      lastName:['', Validators.compose([Validators.required, Validators.maxLength(255)])],
      positionId: [null, Validators.required],
      centerId :[null, Validators.required],
      description: ['', Validators.maxLength(500)],
      phone:[null],
      endDate:[null],
      startDate:[null],
      isActive:this.statusList[0].id,
      departmentId:[null, Validators.required],


    });
    if (this.id) {
      this.getUserDetail(this.id);

    }
  }
  setDataDefault() {
    this.form.patchValue(this.userDetail);
  }
  getUserDetail(id) {
    this.humanResourceService.getInfo(id).subscribe(
      res=>{
        this.userDetail=res.data;
        this.setDataDefault();
      },error => {
        this.userDetail=null;
      }
    )
  }
  get formControl() {
    return this.form.controls;
  }

  closeModal() {
    // if (this.checkFormValueChanges(this.form.value) && this.type !== 'detail') {
    //   this.isModalConfirmShow = true;
    //   const modalRef = this.modalService.open(ConfirmModalComponent, {centered: true, backdrop: 'static'});
    //   modalRef.componentInstance.type = 'confirm';
    //   modalRef.componentInstance.onCloseModal.subscribe(value => {
    //     if (value === true) {
    //       this.activeModal.dismiss('Cross click');
    //     }
    //     this.isModalConfirmShow = false;
    //   });
    // } else {
    // this.activeModal.dismiss('Cross click');
    // this.form.reset();
    // this.activeModal.dismiss(true);
    // }
    this.activeModal.dismiss(false);
  }

  onSubmitData() {
    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      console.warn("invalid");
      return;
    }
    if (this.isDuplicateEmail) {
      console.warn("duplicate");
      return;
    }
    if (this.type==='add') {
      if(this.isDuplicateUsername){
      console.warn("duplicate");
      return;
    }}
    this.spinner.show();
    this.form.value.startDate=this.datepipe.transform(this.form.value.startDate, 'yyyy-MM-dd');
    this.form.value.endDate =this.datepipe.transform(this.form.value.endDate, 'yyyy-MM-dd');

    if (this.form.value.startDate>this.form.value.endDate){

      this.toastService.openErrorToast('ngày bắt đầu làm việc phải nhỏ hơn thời gian kết thúc làm việc')
      this.spinner.hide();
     return ;

    }
      this.humanResourceService.save(this.form.value).subscribe(
        res=>{
          this.toastService.openSuccessToast(this.type==='add'?'Thêm mới thành công':'Sửa thành công');
          this.activeModal.dismiss(true);
          /*this.spinner.hide();*/
        },
        error => {
          this.toastService.openErrorToast(this.type==='add'?'Thêm mới thất bại':'Sửa thất bại');
          this.spinner.hide();
        },
        ()=>{
          this.spinner.hide();
        }
      );
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
    this.sysUserService
      .getUnit({
        name: term,
        limit: ITEMS_PER_PAGE,
        page: 0
      })
      .subscribe((res: HttpResponse<any[]>) => {
        if (res && res.status === STATUS_CODE.SUCCESS && this.unitSearch) {
          this.listUnit$ = of(res.body['content'].sort((a, b) => a.name.localeCompare(b.name)));
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
    if (!this.form.value.name) {
      this.listUnit$ = of([]);
      this.unitSearch = '';
    }
  }

  customSearchFn(term: string, item: any) {
    const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    const newRegEx = new RegExp(replacedKey, 'gi');
    const purgedPosition = item.name.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    return newRegEx.test(purgedPosition);
  }

  // onChangePosition(event) {
  //   if (event) {
  //     this.setValueToField('positionId', event.id);
  //     this.setValueToField('positionName', event.name);
  //   }
  // }
  //
  // setValueToField(item, data) {
  //   this.form.get(item).setValue(data);
  // }
  //
  // onClearPosition() {
  //   this.setValueToField('positionId', null);
  //   this.setValueToField('positionName', null);
  // }


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




  getDeparmentList() {
    this.humanResourceService.getDepartment({  type: "DEPART"}
    ).subscribe(
      res => {
        if (res) {
          this.departmentList = res;
        } else {
          this.departmentList = [];
        }
      },
      err => {
        this.departmentList = [];
      }
    );
  }
  getPositionList() {
    this.humanResourceService.getDepartment({  type: "POSITION"}
    ).subscribe(
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

  getCenterList(){
    this.humanResourceService.getCenterList().subscribe(
      res => {
        if (res) {
          this.centerList= res.data;
        }else{
          this.centerList =[];
        }
      },
      error => {
        this.centerList =[];
      }
    );
  }

  getValueOfField(item) {
    return this.form.get(item).value;
  }

  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  onBlurEmail(field) {
    this.setValueToField(field, this.getValueOfField(field).trim());
    if (!REGEX_PATTERN.EMAIL.test(this.getValueOfField(field))) {
      if (this.getValueOfField(field) !== '') {
        this.form.controls[field].setErrors({invalid: true});
      }
    } else {
      if (this.getValueOfField(field) !== '') {
        // check trùng
        if(this.type==='add'||(this.type==='update'&&this.userDetail.email!==this.form.value.email)){
        this.humanResourceService.checkEmail(this.form.value.email).subscribe(res => {
            this.isDuplicateEmail = false;
          }, err=>{
          this.isDuplicateEmail=true;
        });
        }
      }
    }
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

  onBlurUsername(){
    if(this.type==='add') {
      this.humanResourceService.checkUsername(this.form.value.username).subscribe(res => {
        this.isDuplicateUsername = false;
      }, err => {
        this.isDuplicateUsername = true;
      });
    }
  }


  onCancel() {
    if (this.type === 'update') {
      if (
        this.form.value.humanResourceId === this.userDetail.humanResourceId &&
        this.form.value.email === this.userDetail.email &&
        this.form.value.description === this.userDetail.description&&
        this.form.value.username===this.userDetail.username&&
        this.form.value.firstName===this.userDetail.firstName&&
        this.form.value.lastName===this.userDetail.lastName&&
        this.form.value.centerId===this.userDetail.centerId&&
        this.form.value.phone===this.userDetail.phone&&
        this.form.value.endDate===this.userDetail.endDate&&
        this.form.value.startDate===this.userDetail.startDate&&
        this.form.value.isActive===this.userDetail.isActive&&
        this.form.value.departmentId===this.userDetail.departmentId&&
        this.form.value.positionId===this.userDetail.positionId

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
        this.form.value.humanResourceId === null &&
        this.form.value.email === '' &&
        this.form.value.description === ''&&
        this.form.value.username===''&&
        this.form.value.firstName===''&&
        this.form.value.lastName===''&&
        this.form.value.centerId===null&&
        this.form.value.phone===null&&
        this.form.value.endDate===null&&
        this.form.value.startDate===null&&
        this.form.value.isActive===this.statusList[0].id&&
        this.form.value.departmentId===null&&
        this.form.value.positionId===null
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
    if(this.type==='detail'){
      this.closeModal();
    }
  }

}
