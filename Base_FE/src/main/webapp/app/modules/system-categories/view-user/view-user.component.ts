import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ITEMS_PER_PAGE, MAX_SIZE_PAGE } from 'app/shared/constants/pagination.constants';
import { ActivatedRoute, Router } from '@angular/router';
import { HeightService } from 'app/shared/services/height.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmModalComponent } from 'app/shared/components/confirm-modal/confirm-modal.component';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { OrganizationCategoriesService } from 'app/core/services/system-management/organization-categories.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, of, Subject, Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { debounceTime } from 'rxjs/operators';
import { TIME_OUT } from 'app/shared/constants/set-timeout.constants';
import { ToastService } from 'app/shared/services/toast.service';
import { REGEX_PATTERN } from 'app/shared/constants/pattern.constants';
import { LinkRedmineComponent } from './linkRedmine/linkRedmine.component';
import { edituserModel } from 'app/core/models/system-categories/edit-user.model';
import { OrganizationCategoriesModel } from 'app/core/models/system-categories/organization-categories.model';
import { ModalComponent } from 'app/modules/system-categories/organization-categories/modal/modal.component';
@Component({
  selector: 'jhi-view-user',
  templateUrl: './view-user.component.html',
  styleUrls: ['./view-user.component.scss']
})
export class ViewUserComponent implements OnInit {
  @Input() public selectedData;
  @Input() type;
  form: FormGroup;
  height: number;
  itemsPerPage: any;
  maxSizePage: any;
  routeData: any;
  page: any;
  second: any;
  totalItems: any;
  previousPage: any;
  predicate: any;
  reverse: any;
  checkDelete = false;
  listId: any[];
  listA: any[];
  listUnit$ = new Observable<any[]>();
  parentOrganizationList = new Observable<any[]>();
  // groupOrganizationList = new Observable<any[]>();
  groupOrganizationList: any[] = [];
  groupOrganizationList2:any[]=[];

  unitSearch;

  listUnit7$ = new Observable<any[]>();
  parentOrganizationList1 = new Observable<any[]>();
  // groupOrganizationList = new Observable<any[]>();
  groupOrganizationList1: any[] = [];
  unitSearch7;


  debouncer: Subject<string> = new Subject<string>();
  listUnit1$ = new Observable<any[]>();
  unitSearch1;
  debouncer1: Subject<string> = new Subject<string>();
  listUnit2$ = new Observable<any[]>();
  unitSearch2;
  debouncer2: Subject<string> = new Subject<string>();
  listUnit3$ = new Observable<any[]>();
  unitSearch3;
  debouncer3: Subject<string> = new Subject<string>();
  listUnit4$ = new Observable<any[]>();
  unitSearch4;
  debouncer4: Subject<string> = new Subject<string>();
  listUnit5$ = new Observable<any[]>();
  unitSearch5;
  debouncer5: Subject<string> = new Subject<string>();
  listUnit6$ = new Observable<any[]>();
  unitSearch6;
  debouncer6: Subject<string> = new Subject<string>();
  searchForm: any;
  eventSubscriber: Subscription;
  organizationCategoriesModel:[];
  organizationList: any[];
  rBaM:number ;
  rpmT:number ;
  edit: edituserModel[];
  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  message = '';
  projectId:number;
  disabled: Boolean=true;
  fileInfos: Observable<any>;
  project: OrganizationCategoriesModel;
  attachDoc: OrganizationCategoriesModel[];
  model: any;
  description = '';
  nameProject: string;
  constructor(
    private activatedRoute: ActivatedRoute,
    private heightService: HeightService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private translateService: TranslateService,
    private spinner: NgxSpinnerService,
    private toastService: ToastService,
    private organizationCategoriesService: OrganizationCategoriesService,
    private eventManager: JhiEventManager,
    private _http: HttpClient,
    private router: Router,
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
    this.activatedRoute.queryParams.subscribe( param =>{
      //console.log(param)
      this.projectId=param.id;
    })
  }

  ngOnInit() {



    this.searchForm = {};
    this.buidForm();
    this.onResize();
    this.searchHandle();
    this.registerChange();
    // this.debounceOnSearch();
    this.debounceOnSearch1();
    this.debounceOnSearch2();
    this.debounceOnSearch3();
    this.debounceOnSearch4();
    this.debounceOnSearch5();
    this.debounceOnSearch6();
    this.getDataDropdown();
    this.getDataDropdown1();
    this.getDataDropdown2();
    this.rBaM=0;
    this.rpmT=0;
    const dataProject={
      projectId:this.projectId,
    }
    this.organizationCategoriesService.getProjectById(dataProject).subscribe(res=>{
      this.attachDoc=res.data[0].lstAttachDocument;
      this.nameProject= res.data[0].name;
      this.model = res.data[0];
        console.warn("manhhihi",res.data);
        console.warn("manhhihi3", res.data[0].code);
      this.form.patchValue(res.data[0]);
         //this.project=res.data[0];
          this.form.get('code').setValue(res.data[0].code);
          this.form.get('name').setValue(res.data[0].name);

         const dataTemp = [{
          humanResourceId:res.data[0].pmId,
          firstName:res.data[0].pmName
         }]
         this.listUnit1$ = of(dataTemp);


         const dataTemp2=[{
            humanResourceId:res.data[0].baId,
            firstName:res.data[0].bmName
         }]
         this.listUnit2$ = of(dataTemp2);


         const dataTemp3=[{
          humanResourceId:res.data[0].testLeadId,
          firstName:res.data[0].testLeaderName
         }]
         this.listUnit3$=of(dataTemp3);


         const dataTemp4=[{
          humanResourceId:res.data[0].qaId,
          firstName:res.data[0].qmName
         }]
         this.listUnit4$=of(dataTemp4);




         const dataTemp5=[{
          id:res.data[0].partnerID,
          name:res.data[0].partnerCode
         }]
         this.listUnit5$=of(dataTemp5);

          this.form.get("ba").setValue(res.data[0].ba === 1 ? true : false);
          this.form.get("dev").setValue(res.data[0].dev === 1 ? true : false);
          this.form.get("test").setValue(res.data[0].test === 1 ? true : false);
          this.form.get("BaM").setValue(res.data[0].baMan=== 1 ? "1" : "0" );

          const dataTemp6=[{
            id:res.data[0].statusDetail,
            name:res.data[0].statusDetailName
          }]
          this.listUnit6$=of(dataTemp6);
          //this.form.get("BaM").setValue("0");
          //this.form.get('pmName').setValue(res.data[0].pmName);
          //this.form.get('baId').setValue(res.data[0].ba);
          //this.form.get('testLeadId').setValue(res.data[0].testLeaderName);
          //this.form.get('qaId').setValue(res.data[0].qaId);
          //this.form.get('partnerId').setValue(res.data[0].partnerCode);
          this.form.get('customerPmName').setValue(res.data[0].customerPmName);
          this.form.get('amName').setValue(res.data[0].amName);
          this.form.get('amEmail').setValue(res.data[0].amEmail);
          this.form.get('month').setValue(res.data[0].month);
          this.form.get('dateExpected').setValue(res.data[0].dateExpected);
          // this.form.get('statusOverview').setValue(res.data[0].statusOverview);
          // this.form.get('statusDetail').setValue(res.data[0].statusDetail);
           //this.form.get('startDate').setValue(res.data[0].startDate);
           //this.form.get('endDate').setValue(res.data[0].endDate);
          // this.form.get('statusPayment').setValue(res.data[0].statusPaymentName);
          this.form.get('description').setValue(res.data[0].description);
          this.description = res.data[0].description;
          this.form.get('estimatePrelimiinary').setValue(res.data[0].estimatePrelimiinary);
          this.form.get('estimateActual').setValue(res.data[0].estimateActual);
          // console.warn("manhhihi4",res.data[0].estimateActual);
          this.form.get('estimateOffer').setValue(res.data[0].estimateOffer);
          console.warn("manhhihi4",res.data[0].estimateOffer);
          this.form.get('estimateLatch').setValue(res.data[0].estimateLatch);

         //console.warn("manhhihi2", this.project);


    });
  }


  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {
    this.progress = 0;

    this.currentFile = this.selectedFiles.item(0);
    console.warn('2222',this.currentFile);
    this.organizationCategoriesService.upload(this.currentFile).subscribe(
      event => {
        console.warn('1',event);
      });

    this.selectedFiles = undefined;
  }



  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  get formControl() {
    return this.form.controls;
  }


  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }


  checkCodeExist(code) {
    // this.organizationCategoriesService.checkCodeExist(this.form.value.code).subscribe(
    //   res => {
    //       this.checkCode=res.body;
    //   },
    //   error => {

    //   })
  }
  getValueOfField(item) {
    return this.form.get(item).value;
  }



  onBlurEmail(field) {
    this.setValueToField(field, this.getValueOfField(field).trim());
    if (!REGEX_PATTERN.EMAIL.test(this.getValueOfField(field))) {
      if (this.getValueOfField(field) !== '') {
        this.form.controls[field].setErrors({ invalid: true });
      }
    }
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



  onClearUnit() {
    this.listUnit$ = of([]);
    this.unitSearch = '';
  }

  onSearchUnitClose() {
    if (!this.form.value.groupName) {
      this.listUnit$ = of([]);
      this.unitSearch = '';
    }
  }



  onSearchUnit6(event) {
    // eslint-disable-next-line no-debugger
    //debugger;
    this.unitSearch6 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer6.next(term);
    } else {
      this.listUnit6$ = of([]);
    }
  }
  debounceOnSearch6() {
    this.debouncer6.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit6(value));
  }
  loadDataOnSearchUnit6(term) {
    // eslint-disable-next-line no-debugger
   // debugger;
    const data = {
      keySearch: term,
      type: 'STATUSD'
    }

    this.organizationCategoriesService.getPartnerInfo(data).subscribe(res =>   {
      if (this.unitSearch6) {
        const dataRes: any = res;
        this.listUnit6$ = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));

      } else {
        this.listUnit6$ = of([]);
      }
    });
  }
  onClearUnit6() {
    this.listUnit6$ = of([]);
    this.unitSearch6 = '';
  }

  onSearchUnitClose6() {
    if (!this.form.value.name) {
      this.listUnit6$ = of([]);
      this.unitSearch6 = '';
    }
  }

  onSearchUnit1(event) {
    this.unitSearch1 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer1.next(term);
    } else {
      this.listUnit1$ = of([]);
    }
  }
  debounceOnSearch1() {
    this.debouncer1.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit1(value));
  }
  loadDataOnSearchUnit1(term) {
    // eslint-disable-next-line no-console,no-debugger
    //debugger;
    // eslint-disable-next-line no-console
    console.log(term);
    const data = {
      keySearch: term,
      type: 'PM'
    }
    this.organizationCategoriesService.getParents(data).subscribe((res => {
      if (this.unitSearch1) {
        const dataRes: any = res;
        this.listUnit1$ = of(dataRes.sort((a, b) => a.firstName.localeCompare(b.firstName)));
        // this.listUnit1$ = of(dataRes.sort((a, b) => a.code.localeCompare(b.code)));
        this.listUnit1$ = of(dataRes.sort((a, b) => a.email.localeCompare(b.email)));
      } else {
        this.listUnit1$ = of([]);
      }
    }));
  }
  onClearUnit1() {
    this.listUnit1$ = of([]);
    this.unitSearch1 = '';
  }

  onSearchUnitClose1() {
    if (!this.form.value.name) {
      this.listUnit1$ = of([]);
      this.unitSearch1 = '';
    }
  }

  onSearchUnit2(event) {
    // eslint-disable-next-line no-debugger
    debugger;
    this.unitSearch2 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer2.next(term);
    } else {
      this.listUnit2$ = of([]);
    }
  }
  debounceOnSearch2() {
    this.debouncer2.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit2(value));
  }
  loadDataOnSearchUnit2(term) {
    // eslint-disable-next-line no-debugger
    debugger;
    const data = {
      keySearch: term,
      type: 'BM'
    }

    this.organizationCategoriesService.getParents(data).subscribe(res =>   {
      if (this.unitSearch2) {
        const dataRes: any = res;
        this.listUnit2$ = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));

      } else {
        this.listUnit2$ = of([]);
      }
    });
  }
  onClearUnit2() {
    this.listUnit2$ = of([]);
    this.unitSearch2 = '';
  }

  onSearchUnitClose2() {
    if (!this.form.value.name) {
      this.listUnit2$ = of([]);
      this.unitSearch2 = '';
    }
  }


  onSearchUnit3(event) {
    this.unitSearch3 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer3.next(term);
    } else {
      this.listUnit3$ = of([]);
    }
  }
  debounceOnSearch3() {
    this.debouncer3.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit3(value));
  }
  loadDataOnSearchUnit3(term) {
    const data = {
      keySearch: term,
      type: 'TL'
    }

    this.organizationCategoriesService.getParents(data).subscribe(res =>   {
      if (this.unitSearch3) {
        const dataRes: any = res;
        this.listUnit3$ = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));

      } else {
        this.listUnit3$ = of([]);
      }
    });
  }
  onClearUnit3() {
    this.listUnit3$ = of([]);
    this.unitSearch3 = '';
  }
  onSearchUnitClose3() {
    if (!this.form.value.name) {
      this.listUnit3$ = of([]);
      this.unitSearch3 = '';
    }
  }


  onSearchUnit4(event) {
    this.unitSearch4 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer4.next(term);
    } else {
      this.listUnit4$ = of([]);
    }
  }
  debounceOnSearch4() {
    this.debouncer4.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit4(value));
  }
  loadDataOnSearchUnit4(term) {
    const data = {
      keySearch: term,
      type: 'QM'
    }

    this.organizationCategoriesService.getParents(data).subscribe(res =>   {
      if (this.unitSearch4) {
        const dataRes: any = res;
        this.listUnit4$ = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));
        this.listUnit4$ = of(dataRes.sort((a, b) => a.code.localeCompare(b.code)));
      } else {
        this.listUnit4$ = of([]);
      }
    });
  }
  onClearUnit4() {
    this.listUnit4$ = of([]);
    this.unitSearch4 = '';
  }

  onSearchUnitClose4() {
    if (!this.form.value.parentName) {
      this.listUnit4$ = of([]);
      this.unitSearch4 = '';
    }
  }


  onSearchUnit5(event) {
    this.unitSearch5 = event.term;
    const term = event.term;
    if (term !== '') {
      this.debouncer5.next(term);
    } else {
      this.listUnit5$ = of([]);
    }
  }
  debounceOnSearch5() {
    this.debouncer5.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit5(value));
  }
  loadDataOnSearchUnit5(term) {
    const data = {
      keySearch: term,
      type: 'PARTNER'
    }

    this.organizationCategoriesService.getPartnerInfo(data).subscribe(res =>   {
      if (this.unitSearch5) {
        const dataRes: any = res;
        this.listUnit5$ = of(dataRes.sort((a, b) => a.name.localeCompare(b.name)));
        this.listUnit5$ = of(dataRes.sort((a, b) => a.code.localeCompare(b.code)));
      } else {
        this.listUnit5$ = of([]);
      }
    });
  }
  onClearUnit5() {
    this.listUnit5$ = of([]);
    this.unitSearch5 = '';
  }

  onSearchUnitClose5() {
    if (!this.form.value.parentName) {
      this.listUnit5$ = of([]);
      this.unitSearch5 = '';
    }
  }




  searchHandle() {
    this.page = this.page - 1;
    this.organizationCategoriesService.search(this.searchForm).subscribe(result => {
      this.organizationCategoriesModel = result.data;
      this.totalItems = result.dataCount;
    });
  }

  getDataDropdown() {
    this.listUnit$ = of([]);
    // this.organizationCategoriesService.getParents().subscribe(result => {
    //   this.parentOrganizationList = result.content;
    // });
    const data = {
      keySearch: '',
      type: 'StatusO'
    }
    this.organizationCategoriesService.getStatusOverviewList(data).subscribe(
      result => {
        if (result) {
          const dataRes: any = result;
          this.groupOrganizationList = dataRes;
        } else {
          this.groupOrganizationList = [];
        }
      },
      err => {
        this.groupOrganizationList = [];
      }
    );
  }



  getDataDropdown2() {
    this.listUnit$ = of([]);
    // this.organizationCategoriesService.getParents().subscribe(result => {
    //   this.parentOrganizationList = result.content;
    // });
    const data = {
      keySearch: '',
      type: 'StatusD'
    }
    this.organizationCategoriesService.getStatusOverviewList(data).subscribe(
      result => {
        if (result) {
          const dataRes: any = result;
          this.groupOrganizationList2 = dataRes;
        } else {
          this.groupOrganizationList2 = [];
        }
      },
      err => {
        this.groupOrganizationList2 = [];
      }
    );
  }



  getDataDropdown1() {
    this.listUnit7$ = of([]);
    // this.organizationCategoriesService.getParents().subscribe(result => {
    //   this.parentOrganizationList = result.content;
    // });
    const data = {
      keySearch: '',
      type: 'StatusP'
    }
    this.organizationCategoriesService.getStatusOverviewList(data).subscribe(
      result => {
        if (result) {
          const dataRes: any = result;
          this.groupOrganizationList1 = dataRes;
        } else {
          this.groupOrganizationList1 = [];
        }
      },
      err => {
        this.groupOrganizationList1 = [];
      }
    );
  }






  doSearch() {
    this.searchForm.code = this.form.value.code;
    this.searchForm.name = this.form.value.name;
    this.searchForm.parentId = this.form.value.parent;
    this.searchForm.organizationGroup = this.form.value.groupOrganization;
    this.searchForm.page = this.page;
    this.searchForm.pageSize = this.itemsPerPage;

    this.organizationCategoriesService.search(this.searchForm).subscribe(response => {
      this.organizationCategoriesModel = response.data;
      this.totalItems = response.dataCount;
    });
  }

  customSearchFn(term: string, item: any) {
    const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    const newRegEx = new RegExp(replacedKey, 'gi');
    const purgedPosition = item.name.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    return newRegEx.test(purgedPosition);
  }

  onChangePosition(event) {
    if (event) {
      this.setValueToField('id', event.id);
      this.setValueToField('name', event.name);
    }
  }

  onClearPosition() {
    this.setValueToField('name', null);
    this.setValueToField('name', null);
  }

  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  onDelete(data) {
    // this.listId = [Number(data.id)];
    // this.organizationCategoriesService.checkDelete(data.id).subscribe(result => {
    //   if (result) {
    //     this.toastService.openErrorToast('Đơn vị này đã được sử dụng , không được phép xóa');
    //   } else {
    //     const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
    //     modalRef.componentInstance.type = 'delete';
    //     modalRef.componentInstance.param = this.translateService.instant('organizationCategories.organization');
    //     modalRef.componentInstance.onCloseModal.subscribe(value => {
    //       if (value === true) {
    //         this.onSubmitDelete(data.id);
    //       }
    //     });
    //   }
    // });
  }

  onSubmitDelete(id: number) {
    this.organizationCategoriesService.delete(id).subscribe(result => {
      // this.fetchData();
      this.page = 0;
      this.doSearch();
      this.toastService.openSuccessToast('Xóa dữ liệu thành công!');
    });
  }

  fetchData() {
    this.organizationCategoriesService.search(this.searchForm).subscribe(result => {
      this.organizationCategoriesModel = result.data;
      this.totalItems = result.dataCount;
    });
  }

  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  changePageSize(size) {
    this.itemsPerPage = size;
    this.transition();
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }
  transition() {
    const formValue = this.form.value;
    this.doSearch();
  }
  registerChange() {
    this.eventSubscriber = this.eventManager.subscribe('organizationChange', response => this.searchHandle());
  }

  private buidForm() {
    this.form = this.formBuilder.group({
      code: [''],
      name: [null, Validators.compose([Validators.required])],
      customerPmName: [''],
      customerPmPhone:[],
      customerEmail:[' '],
      amName: [''],
      amEmail: [''],
      description: [''],
      partnerID: [null, Validators.compose([Validators.required])],
      estimatePrelimiinary: [''],
      estimateActual: [''],
      estimateOffer: [''],
      estimateLatch: [''],
      estimateInternal: [' '],
      startDate: [''],
      endDate: [''],
      month: [''],
      dateExpected: [''],
      pmId: [null, Validators.compose([Validators.required])],
      baId: [null, Validators.compose([Validators.required])],
      qaId: [null, Validators.compose([Validators.required])],
      testLeadId: [null, Validators.compose([Validators.required])],
      statusOverview: [],
      ba: [],
      dev: [],
      test: [],
      statusDetail: [],
      statusPayment: [],
      amPhone:[],
      BaM: [],
      listFileProject:[''],
      file: File,
      //parent: [null],
      //groupOrganization: [null]
    });
  }
  convertDate(str) {
    const date = new Date(str),
      mnth = ('0' + (date.getMonth() + 1)).slice(-2),
      day = ('0' + date.getDate()).slice(-2);
    return [date.getFullYear(), mnth, day].join('/');
  }


  onProfitSelectionChange(data) : void {
   // debugger
  //  this.form.value;
    // if ('BA' === data) {
    //   this.rBaM = 1;
    //   this.rpmT = 0;
    // }

    // if ('Lead' === data) {
    //   this.rBaM = 0;
    //   this.rpmT = 1;
    // }
  };

  // editUser(): void{
  //   console.warn("123456");
  //   const neweditUserModel : edituserModel =new edituserModel();
  //   console.warn("112",this.form.value.ba);
  //   console.warn("113",this.rBaM);
  //   console.warn("114",this.rpmT);
  //   const data = {
  //     projectId: this.projectId,
	//     code : this.form.value.code,
  //     name : this.form.value.name,
  //     partnerID: this.form.value.partnerID,
	//     description : this.form.value.description,
	//     customerPmName : this.form.value.customerPmName,
	//     amName : this.form.value.amName,
  //     amEmail : this.form.value.amEmail,
  //     estimatePrelimiinary: this.form.value.estimatePrelimiinary,
  //     estimateActual: this.form.value.estimateActual,
  //     estimateOffer: this.form.value.estimateOffer,
  //     estimateLatch:this.form.value.estimateLatch,
  //     pmId: this.form.value.pmId,
  //     baId: this.form.value.baId,
  //     qaId: this.form.value.qaId,
  //     ba:this.form.value.ba === true ? 1 : 0,
  //     dev:this.form.value.dev === true ? 1 : 0,
  //     test:this.form.value.test === true ? 1 : 0,
  //     baMan: this.form.value.BaM,
  //     //pmMan:this.rpmT,
  //     testLeadId:this.form.value.testLeadId,
  //      startDate: this.convertDate(this.form.value.startDate),
  //      endDate: this.convertDate(this.form.value.endDate),
  //      statusOverview:this.form.value.statusOverview,
  //      statusDetail:this.form.value.statusDetail,
  //      statusPayment:this.form.value.statusPayment,
  //      month:this.form.value.month,
  //      dateExpected:this.form.value.dateExpected,
  //      //projectId:this.projectId,
  //     lstAttachDocument: [{
  //       code:1,
  //       name:this.currentFile === undefined ? "none" :this.currentFile.name ,

  //     }]
  //   }
  //   // set file path
  //   const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
  //     modalRef.componentInstance.type = 'delete';
  //     modalRef.componentInstance.param = 'muốn sửa dự án';
  //     modalRef.componentInstance.onCloseModal.subscribe(value => {
  //       if (value === true) {
  //         this.organizationCategoriesService.edit(data).subscribe(res => {
  //           console.warn('edit',data);
  //           this.toastService.openSuccessToast('Sửa dữ liệu thành công!');
  //           this.router.navigate(['system-categories/project-management']);
  //           //location.reload();
  //         });
  //       }
  //   });
  // }

  // nuctv 29/07
  openModal(){
    const modalRef = this.modalService.open(LinkRedmineComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
    modalRef.componentInstance.projectID = this.projectId;
    modalRef.componentInstance.action = "view";
  }

  openModalULNL(type){
    if (type === 1) {
      const modalRef = this.modalService.open(ModalComponent, {
        size: 'lg',
        backdrop: 'static',
        keyboard: false,
        windowClass: 'myCustomModalClass'
      });
      modalRef.componentInstance.model = this.model;
      modalRef.componentInstance.type = type;
      modalRef.componentInstance.recorded.subscribe(value => {
        this.model.statusPreliinary = value.statusPreliinary;

      })
    }
    if (type === 2) {
      const modalRef = this.modalService.open(ModalComponent, {
        size: 'lg',
        backdrop: 'static',
        keyboard: false,
        windowClass: 'myCustomModalClass'
      });
      modalRef.componentInstance.model = this.model;
      modalRef.componentInstance.type = type;
      modalRef.componentInstance.recorded.subscribe(value => {
        this.model.statusInternal = value.statusInternal;
      })
    }
    if (type === 3) {
      const modalRef = this.modalService.open(ModalComponent, {
        size: 'lg',
        backdrop: 'static',
        keyboard: false,
        windowClass: 'myCustomModalClass'
      });
      modalRef.componentInstance.model = this.model;
      modalRef.componentInstance.type = type;
      modalRef.componentInstance.recorded.subscribe(value => {
        this.model.statusOffer = value.statusOffer;
      })
    }
    if (type === 4) {
      const modalRef = this.modalService.open(ModalComponent, {
        size: 'lg',
        backdrop: 'static',
        keyboard: false,
        windowClass: 'myCustomModalClass'
      });
      modalRef.componentInstance.model = this.model;
      modalRef.componentInstance.type = type;
      modalRef.componentInstance.recorded.subscribe(value => {
        this.model.statusLatch = value.statusLatch;
      })
    }
  }

  formatDate(date) {
    // console.log(date);
    if(date !== undefined) {
      return date.substring(0, 10);
    }

  }

  closeEdit(){
    this.router.navigate(['system-categories/project-management']);
  }

}
