import { Component, Input, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HeightService } from 'app/shared/services/height.service';
import { SHOW_HIDE_COL_HEIGHT } from 'app/shared/constants/perfect-scroll-height.constants';
import { ITEMS_PER_PAGE, MAX_SIZE_PAGE } from 'app/shared/constants/pagination.constants';
import { ConfirmModalComponent } from 'app/shared/components/confirm-modal/confirm-modal.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OrganizationCategoriesModel } from 'app/core/models/system-categories/organization-categories.model';
import { CommonService } from 'app/shared/services/common.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastService } from 'app/shared/services/toast.service';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { Observable, forkJoin, Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import {OrganizationCategoriesService} from "app/core/services/system-management/organization-categories.service";

@Component({
  templateUrl: './linkRedmine.component.html',
  styleUrls: ['./linkRedmine.component.scss'],
  providers:[OrganizationCategoriesService]
})
export class LinkRedmineComponent implements OnInit {
  @Input() type;
  @Input() action;

  //nuctv 29/07
  @Input() projectID : number;
  form: FormGroup;
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
  formValue: any[];
  isModalConfirmShow = false;
  item;
  code;
  errorLinkList : any=[];
  constructor(
    private http: HttpClient,
    private activatedRoute: ActivatedRoute,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private commonService: CommonService,
    private toastService: ToastService,
    private translateService: TranslateService,
    public activeModal: NgbActiveModal,
    private eventManager: JhiEventManager,
    //nuctv 29/07
    private organizationSerice :OrganizationCategoriesService
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
  getLinkRedmine(){
    this.organizationSerice.getLinkRedmineByprojectID(this.projectID).subscribe(res=>{
      if(null==res.data.link){
        this.item={
          projectID: this.projectID,
          linkProject:'',
          linkBA: '',
          linkDev: '',
          linkTKCT: '',
          linkKBKT: '',
          linkTest:'',
          linkFixBug:''
        }
      } else {
        this.item =res.data.link;
      }
      this.form.patchValue(this.item);
      this.code=res.data.code;
    })
  };
  buildForm() {
    this.form = this.formBuilder.group({
      projectID: this.projectID,
      linkProject:['',Validators.compose([Validators.required, Validators.maxLength(500)])],
      linkBA: ['',Validators.maxLength(500)],
      linkDev: ['',Validators.maxLength(500)],
      linkTKCT: ['',Validators.maxLength(500)],
      linkKBKT: ['',Validators.maxLength(500)],
      linkTest:['',Validators.maxLength(500)],
      linkFixBug:['',Validators.maxLength(500)]
    });
    this.getLinkRedmine()
  }

  ngOnInit() {
    this.buildForm();
    this.onResize();
    // console.log(this.action);
  }
  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }
  onCloseAddModal() {
    if (this.action !== 'view') {
      this.isModalConfirmShow = true;
      const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
      modalRef.componentInstance.type = 'confirm';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.activeModal.dismiss();
        }
        this.isModalConfirmShow = false;
      });
    } else {
      this.activeModal.dismiss();
      this.form.reset();
      this.activeModal.dismiss(true);
    }
  }
  onSubmitData() {
    if (this.form.invalid) {
      this.commonService.validateAllFormFields(this.form);
      return;
    }
    this.spinner.show();
    //check link Redmine
    this.organizationSerice.checkLink(this.form.value).subscribe(res=>{
      this.errorLinkList= res.data;
      if(this.errorLinkList.length > 0){
        this.spinner.hide();
        return;
      }
      this.organizationSerice.updateRedmineLink(this.form.value).subscribe(res1=>{
        this.toastService.openSuccessToast('Sửa link future thành công');
        this.spinner.hide();
        this.activeModal.dismiss();
      },error => {
        this.toastService.openErrorToast('Sửa link future thất bại');
        this.spinner.hide();
      });
    },error => {
      this.toastService.openErrorToast("Không thể kiểm tra link");
      this.spinner.hide();
      return;
    });
  }
  trimSpace(field){
    const value = this.getValueOfField(field);
    if (value) {
      this.form.get(field).setValue(value.trim());
    }
  }
  getValueOfField(item) {
    return this.form.get(item).value;
  }
  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }
  get formControl() {
    return this.form.controls;
  }
}
