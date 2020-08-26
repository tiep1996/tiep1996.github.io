import { Component, Input, OnInit, NgZone, Renderer, Directive } from '@angular/core';
import { ContractManagerService } from 'app/core/services/contract-management/contract-manager.service.ts';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { map, debounceTime } from 'rxjs/operators';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastService } from 'app/shared/services/toast.service';
import { HeightService } from 'app/shared/services/height.service';
import { TranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { NgxSpinnerService } from 'ngx-spinner';
import { ContractManagementService } from 'app/core/services/system-management/contract-management.service';
import createNumberMask from 'text-mask-addons/dist/createNumberMask';
import { Observable, of, Subject } from 'rxjs';
import { TIME_OUT } from 'app/shared/constants/set-timeout.constants';
import { HttpResponse } from '@angular/common/http';
import { ConfirmModalComponent } from 'app/shared/components/confirm-modal/confirm-modal.component';
import { faSleigh } from '@fortawesome/free-solid-svg-icons';
import { REGEX_PATTERN } from 'app/shared/constants/pattern.constants';
import { OrganizationCategoriesModel } from "app/core/models/system-categories/organization-categories.model";
import { CountModel } from "app/core/models/system-categories/count";
import { OrganizationCategoriesService } from "app/core/services/system-management/organization-categories.service";
import { ProjectMember } from "app/core/models/project-management/project-member";
import { CommonService } from 'app/shared/services/common.service';

//  IIST - trangnc - CREATED 200521
// Màn hình thêm, sửa hợp đồng thuê ngoài
@Component({
  selector: 'jhi-add-human',
  templateUrl: './add-human.component.html',
  styleUrls: ['./add-human.component.scss']
})
export class AddHumanComponent implements OnInit {
  @Input() public selectedData: OrganizationCategoriesModel;
  height: number;
  form: FormGroup;
  projectModel: any;
  //
  currentPage = 1;
  numberPerPage = 5;
  previousPage: any;
  pageList: any[] = [];
  numberOfPages = 0;
  numRecord = 0;
  arrNumberOfPages;
  isModalConfirmShow = false;
  unitSearch4;
  startDate: number;
  endDate: number;
  itemsPerPage: any;
  totalItems: any;
  maxSizePage: any;
  lstDelete: any[] = [];
  pageSize: any;
  routeData: any;
  page: any;
  resourcesUsed: "abc";
  //trangnc validate tổng MM start
  type: any;
  organizationCategoriesModel: OrganizationCategoriesModel[];
  countModel: CountModel[];
  lst: any = [];
  regexReplaceSeparate;
  isEdit = [];
  ischeck = true;
  status = false;
  regexReplacePoint;
  // class Error Project Code
  clErrPC: string;
  // class Error tổng MM đã thẩm định
  clErrorMM: string;
  // class Error ULNL
  clErrULNL: string;
  // class Error Năm hợp tác
  clErrYear: string;
  listUnit2$ = new Observable<any[]>();

  debouncer2: Subject<string> = new Subject<string>();
  // class Error Tổng MM thuê ngoài
  clErrorTtal: string;
  // class Error mã đối tác
  clErrorPartner: string;
  // danh sách những bản ghi bị xóa trong bảng ttnltt
  lstdataDelete: any = [];
  // object delete
  objDelete;
  // object delete DB
  objDeleteDB;

  listRole: any[];
  // number LK
  numberLK = 0;
  //
  objCloneAlert: any = {};
  //
  objCloneForm: any = {};

  arrObjCloneAlert: any[] = [];
  projectId: any;

  // button Disable Message Error For Date.
  btnDisMesDate: boolean;

  // trangnc validate tổng MM end
  totalMM = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 15,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tổng MM đã thanh toán
  totalMMPayed = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: false,
    integerLimit: 5,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tổng MM đã thẩm định
  totalAppraisedMM = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 1 MM
  monthValue1 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 2 MM
  monthValue2 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 3 MM
  monthValue3 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 4 MM
  monthValue4 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 5 MM
  monthValue5 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 6 MM
  monthValue6 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 7 MM
  monthValue7 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 8 MM
  monthValue8 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 9 MM
  monthValue9 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 10 MM
  monthValue10 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 11 MM
  monthValue11 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc validate tháng 12 MM
  monthValue12 = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  // trangnc Tổng MM sử dụng luỹ kế
  totalAccumulatedMM = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: false,
    allowDecimal: true,
    decimalLimit: 2,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: false,
    integerLimit: 10,
    decimalSymbol: '.'
  };

  //   trangnc Đơn giá
  price = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: true,
    allowDecimal: false,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 10,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };
  countBa = 0;
  bmName: any;
  countDev = 0;
  pmteamLeadName: any;
  countTest = 0;
  testLeadName: any;
  countQa = 0;
  qmName: any;
  countMember = 0;
  // giá trị hợp đồng
  contractVal = {
    prefix: '',
    suffix: '',
    includeThousandsSeparator: true,
    allowDecimal: true,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: true,
    integerLimit: 15,
    thousandsSeparatorSymbol: ',',
    decimalSymbol: '.'
  };

  //  trangnc
  objClone1: ProjectMember[] = [];
  objClone: any = [];
  unitSearch;
  unitSearch1;
  unitSearch2;
  object: [];
  unitSearch3;

  // search đầu mối kinh doanh
  unitSearchDMKD;
  // search đầu mối kỹ thuật
  unitSearchDMKT;

  //  objPartner
  objPartner;
  //
  debouncer: Subject<string> = new Subject<string>();
  // ẩn edit
  buttonDisabled = false;
  //  danh sách mã đối tác khi search.
  listUnit$ = new Observable<any[]>();
  // ẩn column hành động khi thực hiện view
  hiddenView = false;
  // trangnc list doi tác
  listDT$ = new Observable<any[]>();
  // trangnc list đầu mối đề nghị hợp tác
  listDMDVHT$ = new Observable<any[]>();
  // trangnc list Đầu mối kinh doanh
  listDMKD$ = new Observable<any[]>();
  // trangnc list Đầu mối kỹ thuật
  listDMKT$ = new Observable<any[]>();
  //trangnc list đơn vị sản xuất
  listDVSX$ = new Observable<any[]>();
  //trangnc list đơn vị kinh doanh
  listDVKD$ = new Observable<any[]>();

  // trangnc
  partnerId;
  // trangnc obj chứa data xem - sửa
  objViewOrUpdate: any = {
    partnerId: '', // mã dự án
    signatureStatus: '', //  trạng thái ký
    ttrpakdNumber: '', //  số TTrParkd
    contractStatus: '', //  trạng thái hợp đồng
    contractType: '', //  loại hợp đồng
    signDate: '', //  ngày ký hợp đồng
    startTime: '', //  thời gian bắt đầu
    endTime: '', //  thời gian kết thúc
    totalOwnedMM: '', //  tổng MM thuê ngoài
    contractValue: '', //  giá trị hợp đồng
    //  objView.ownedAmount = res.ownedAmount, //   Số tiền còn nợ (tự tính)

    contractDescription: '', //  thông tin chung hợp đồng
    note: '', //  ghi chú
    price: '', //  đơn giá
    //  objView.totalAccumulatedMM = res.totalAccumulatedMM, //  Tổng MM sử dụng luỹ kế
    totalMMPayed: '', //  Tổng MM đã thanh toán
    totalMM: '' //  Tổng MM thuê ngoài
  };

  // trangnc DropList Trạng thái ký
  signatureStatusList: any = [];
  // trangnc DropList Trạng thái hợp đồng
  contractStatusList: any = [];
  // trangnc DropList Loại hợp đồng
  contractTypeList: any = [];
  // trangnc biến Form
  signatureStatus: any;
  // trangnc TTr PAKD
  ttrpakdNumber: any;
  // trangnc Trạng thái hợp đồng
  contractStatus: any;
  // trangnc Loại hợp đồng
  contractType: any;
  // trangnc ngày ký hợp đồng
  signDate: any;
  //  trangnc thời gian bắt đầu
  startTime;
  // trangnc thời gian kết thúc
  endTime;
  // trangnc MM còn nợ
  totalOwnedMM; // (this.totalMM == null ? 0 : Number(this.totalMM)) - (this.totalMMPayed == null ? 0 : Number(this.totalMMPayed));
  // trangnc Giá trị hợp đồng
  contractValue = 0;
  //  trangnc Số tiền còn nợ
  ownedAmount;
  //  trangnc ẩn-hiện
  hidden = false;
  // trangnc ẩn 2 trường TTrPark và ngày ký hợp đồng
  updateHidden = false;

  //  Biến Thông tin nỗ lực sử dụng thực tế
  //  trangnc Tổng MD sử dụng thực tế
  totalRealityMD = 0;

  isDisabledContractValue = false;
  error = null;
  errorMMTN = null;
  errorPartner = null;
  errortotalMM = null;
  lstHuman: any[] = [];
  message = null;
  messagetotalMM = null;
  messageDate = null;
  messageErrorPartner = null;
  tempDropdownDataList: any = [];
  dropdownDataList: any = [];
  dropdownDataMap: any = {};
  activeEffort = true;
  decimalPointSpace;
  decimalPointSignSeparate;
  //  disable MM còn nợ
  disabledMMConNo = true;
  ablecontractValue;

  params: any = {
    id: null,
    partnerId: null,
    partnerName: null,
    signatureStatus: 'Chưa Ký',
    ttrpakdNumber: null,
    contractStatus: null,
    contractType: null,
    startTime: null,
    endTime: null,
    signDate: null,
    // totalMMPayed: null,
    totalOwnedMM: null,
    contractValue: null,
    ownedAmount: null,
    actualEffortList: [],
    actualEffortList2: [],

  };

  //  object cho insert Data
  objectInsert: any = {};

  /* ------ FAKE DATA--------------------------------------------- */
  //  trangnc Kế hoạch thuê ngoài
  outsourcePlans: any = [];
  //  trangnc trạng thái thẩm định
  effortStatusList: any = [];
  // trangnc năm hơp tác
  cooperationYearList: any = [];
  //  check title 0: add - 1: xem - 2: sửa
  checkTile;
  //   Đơn vị thuê ngoài - Đơn vị kinh doanh - Đơn vị sản xuất
  organizationList;
  //  Đối tác
  //   Mã dự án
  tempSoftwareDevelopmentProjects: any = [];
  //  Đầu mối
  tempClueList: any = [];
  sysUserService: any;

  /* ------END FAKE DATA--------------------------------------------- */

  constructor(
    private modalService: NgbModal,
    private spinner: NgxSpinnerService,
    private formBuilder: FormBuilder,
    private contractManagerService: ContractManagerService,
    private service: ContractManagementService,
    private route: ActivatedRoute,
    private router: Router,
    private toastService: ToastService,
    private translateService: TranslateService,
    private heightService: HeightService,
    private datePipe: DatePipe,
    private commonService: CommonService,
    // public activeModal: NgbActiveModal,
    private ngZone: NgZone,
    private organizationCategoriesService: OrganizationCategoriesService,
    private renderer: Renderer,
    // private activatedRoute: ActivatedRoute
  ) {
    // this.routeData = this.activatedRoute.data.subscribe(data => {
    //   console.log(data);
    // });
    
    // this.activatedRoute.queryParams.subscribe( param =>{
    //   console.log(param);
    // })
  }

  countProjectMember(type, depart, projectId, role) {
    this.organizationCategoriesService.countProjectMember(depart, projectId, role).subscribe(res => {
      if (res && res.body) {
        if (type === "ba") {
          this.countBa = res.body.countProjectMember;
        }
        if (type === "dev") {
          this.countDev = res.body.countProjectMember;
        }

        if (type === "test") {
          this.countTest = res.body.countProjectMember;
        }

        if (type === "qa") {
          this.countQa = res.body.countProjectMember;
        }

        if (type === "cty") {
          this.countMember = res.body.countProjectMember;
        }
      }
    });

  }

  nameMember(type, depart, projectId, role) {
    this.organizationCategoriesService.countProjectMember(depart, projectId, role).subscribe(res => {
      if (res && res.body) {
        if (role === "BM") {
          this.bmName = res.body.fullName;
        }
        if (role === "PM") {
          this.pmteamLeadName = res.body.fullName;
        }

        if (role === "TL") {
          this.testLeadName = res.body.fullName;
        }

        if (role === "QM") {
          this.qmName = res.body.fullName;
        }
      }
    });

  }


  ngOnInit(): void {
    this.selectedData = this.commonService.getDataTranfer('projectData');
    this.commonService.clearDataTranfer;
    this.countProjectMember("ba", "BA", this.selectedData.projectId, "");
    this.countProjectMember("qa", "QA", this.selectedData.projectId, "");
    this.countProjectMember("test", "Tester", this.selectedData.projectId, "");
    this.countProjectMember("dev", "Dev", this.selectedData.projectId, "");
    this.countProjectMember("cty", "", this.selectedData.projectId, "");
    this.nameMember("", "", this.selectedData.projectId, "PM");
    this.nameMember("", "", this.selectedData.projectId, "BM");
    this.nameMember("", "", this.selectedData.projectId, "TL");
    this.nameMember("", "", this.selectedData.projectId, "QM");
    this.startDate = new Date().setDate(1);
    this.endDate = new Date().setDate(1);
    this.buidForm();
    this.type = '';
    this.buttonDisabled = false;
    this.listDT$ = of([]);
    this.setDecimal();
    this.getAllRole();
    this.debounceOnSearch2();
    this.loadAlProjectMember();
    this.onResize();
    this.contractManagerService.currentInvoice.subscribe(res => {
      
      this.type = res.type;
      if (res.type === 'view') {
        //  hiển thị xem
        //  hiển thị 2 trường TTr PAKD - Ngày ký hợp đồng
        this.updateHidden = false;
        this.hiddenView = true;
        this.onResize();

        this.getSignStatus();
        this.getTypeContracts();
        this.getContractStatus();
        this.getAllPlan();
        this.getULNL();
        this.getAllYear();
        this.pushDataToForm(res, 'view');
        this.getDataToactualEffortList(res.id);
        //  this.convertDataLstToactualEffortList();
        this.checkTile = 1;
      } else if (res.type === 'update') {
        //  thực hiện sửa
        //  ẩn 2 trường TTr PAKD - Ngày ký hợp đồng
        this.checkTile = 2;
        this.updateHidden = true;
        this.hiddenView = false;
        this.onResize();
        this.getSignStatus();
        this.getTypeContracts();
        this.getContractStatus();
        this.getAllPlan();
        this.getULNL();
        this.getAllYear();
        this.pushDataToForm(res, 'update');
        this.getDataToactualEffortList(res.id);
        this.cloneObject(res);
        // this.arrPage();
      } else if (res.type === 'add') {
        //  thực hiện thêm mới
        //  hiển thị 2 trường TTr PAKD - Ngày ký hợp đồng
        this.updateHidden = false;
        this.form.get('type').setValue('add');
        this.onResize();

        this.getContractStatus();
        this.getTypeContracts();
        this.getSignStatus();
        this.debounceOnSearch();
        this.checkTile = 0;
      } else if (res.type === 'create') {
        //  thực hiện thêm mới
        //  hiển thị 2 trường TTr PAKD - Ngày ký hợp đồng
        this.updateHidden = false;
        this.form.get('type').setValue('create');
        this.onResize();

        this.getContractStatus();
        this.getTypeContracts();
        this.getSignStatus();
        this.debounceOnSearch();
        this.getAllPlan();
        this.getULNL();
        this.getAllYear();
        this.checkTile = 0;
        this.lst = res.data;
        this.convertDataLstToactualEffortList();
        //
      }
    });
  }


  // trangnc lấy danh sách trạng thái ký
  getSignStatus() {
    this.contractManagerService.getSignStatusOrContractStatus('TTK').subscribe(res => (this.signatureStatusList = res));
  }

  // trangnc lấy danh sách loại hợp đồng
  getTypeContracts() {
    this.contractManagerService.getSignStatusOrContractStatus('LHD').subscribe(res => (this.contractTypeList = res));
  }

  // trangnc lấy danh sách trạng thái hợp đồng
  getContractStatus() {
    this.contractManagerService.getSignStatusOrContractStatus('TTHD').subscribe(res => (this.contractStatusList = res));
  }

  //  trangnc lấy danh sách	Trạng thái thẩm định ULNL
  getULNL() {
    this.contractManagerService.getSignStatusOrContractStatus('ULNL').subscribe(res => (this.effortStatusList = res));
  }

  onchangePartner(event) { }
  //  trangnc
  private buidForm() {
    this.form = this.formBuilder.group({

      id: '', //  id bản ghi
      partnerId: '', //  (id) đối tác
      partnerCode: '', //  mã đối tác
      partnerName: '', //  tên dự án
      signatureStatus: 18, //  trạng thái ký
      ttrpakdNumber: '', //  số TTrParkd
      contractStatus: '', //  trạng thái hợp đồng
      contractType: '', //  loại hợp đồng
      startTime: '', //  thời gian bắt đầu
      endTime: '', //  thời gian kết thúc
      signDate: '', //  ngày ký hợp đồng
      startDate: '',
      endDate: '',
      // totalMMPayed: null,
      totalOwnedMM: 0, //  tổng MM thuê ngoài
      contractValue: 0, //  giá trị hợp đồng
      ownedAmount: 0, //   Số tiền còn nợ
      actualEffortList: [], //  thông tin năng lực đối tác
      contractDescription: '', //  thông tin chung hợp đồng
      note: '', //  ghi chú
      price: '', //  đơn giá
      totalAccumulatedMM: '', //  Tổng MM sử dụng luỹ kế
      totalMMPayed: '', //  Tổng MM đã thanh toán
      totalMM: 0, //  ổng MM thuê ngoài
      type: '', //  kiểu
      outsourcingContractId: '', // id hợp đồng
      fullnameBa: '',
      humanResourcesId: [null, Validators.compose([Validators.required])],
      roleId: [null, Validators.compose([Validators.required])],
    });
  }

  cloneObject(res: any) {
    this.objCloneAlert.id = res.id;
    this.objCloneAlert.partnerId = res.partnerId;
    this.objCloneAlert.partnerCode = res.partnerCode;
    this.objCloneAlert.signatureStatus = res.signatureStatus;
    this.objCloneAlert.ttrpakdNumber = res.ttrpakdNumber;
    this.objCloneAlert.contractStatus = res.contractStatus;
    this.objCloneAlert.contractType = res.contractType;
    //this.objCloneAlert.createDate = res.createDate;
    this.objCloneAlert.startTime = res.startTime;
    this.objCloneAlert.endTime = res.endTime;
    this.objCloneAlert.totalMM = res.totalMM;
    this.objCloneAlert.totalMMPayed = res.totalMMPayed;
    //this.objCloneAlert.totalOwnedMM = Number(res.totalMM) - Number(res.totalMMPayed);
    this.objCloneAlert.mmSumLK = res.mmSumLK;
    this.objCloneAlert.price = res.price;
    this.objCloneAlert.contractValue = res.contractValue;
    this.objCloneAlert.getContractDescription = res.getContractDescription;
    this.objCloneAlert.getContractDescription = res.note;

    this.arrObjCloneAlert = this.params.actualEffortList;
  }

  objForm() {
    const val = this.form.value;
    this.objCloneForm.id = val.id;
    this.objCloneForm.partnerId = val.partnerId;
    this.objCloneForm.partnerCode = val.partnerCode;
    this.objCloneForm.signatureStatus = val.signatureStatus;
    this.objCloneForm.ttrpakdNumber = val.ttrpakdNumber;
    this.objCloneForm.contractStatus = val.contractStatus;
    this.objCloneForm.contractType = val.contractType;
    // this.objCloneForm.createDate = val.createDate;
    this.objCloneForm.startTime = val.startTime;
    this.objCloneForm.endTime = val.endTime;
    this.objCloneForm.totalMM = val.totalMM;
    this.objCloneForm.totalMMPayed = val.totalMMPayed;
    // this.objCloneForm.totalOwnedMM = Number(val.totalMM) - Number(val.totalMMPayed);
    this.objCloneForm.mmSumLK = val.totalAccumulatedMM;
    0;
    this.objCloneForm.price = val.price;
    this.objCloneForm.contractValue = val.contractValue;
    this.objCloneForm.getContractDescription = val.getContractDescription;
    this.objCloneForm.getContractDescription = val.note;
  }

  onCkeck() {
    this.objForm();
    if (JSON.stringify(this.objCloneAlert) === JSON.stringify(this.objCloneForm)) {
      if (this.arrObjCloneAlert.length === this.params.actualEffortList.length) {
        const size = this.arrObjCloneAlert.length;
        for (let i = 0; i < size; i++) {
          if (JSON.stringify(this.arrObjCloneAlert[i]) !== JSON.stringify(this.params.actualEffortList[i])) {
            return false;
          }
        }
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  //  trangnc thực hiện đổ data tới actualEffortList
  getDataToactualEffortList(id: any) {
    this.contractManagerService.getListBycontractID(id).subscribe(res => {
      this.lst = res.data;
      this.convertDataLstToactualEffortList();
    });
  }

  private convertDataLstToactualEffortList() {
    if (this.lst[0].outsourcingContractId === undefined || this.lst[0].outsourcingContractId === 'undefined') {
      this.form.value.outsourcingContractId = '';
    } else {
      this.form.value.outsourcingContractId = this.lst[0].outsourcingContractId;
    }
    for (let i = 0; i < this.lst.length; i++) {
      const obj: any = {};
      if (this.lst[i].monthValues !== null && this.lst[i].monthValues !== undefined) {
        const res = this.lst[i].monthValues.split(';');
        let sum = 0;
        for (let index = 0; index < res.length; index++) {
          sum += Number(res[index]);
        }
        (obj.totalRealityMD = sum),
          (obj.monthValue1 = res[0]),
          (obj.monthValue2 = res[1]),
          (obj.monthValue3 = res[2]),
          (obj.monthValue4 = res[3]),
          (obj.monthValue5 = res[4]),
          (obj.monthValue6 = res[5]),
          (obj.monthValue7 = res[6]),
          (obj.monthValue8 = res[7]),
          (obj.monthValue9 = res[8]),
          (obj.monthValue10 = res[9]),
          (obj.monthValue11 = res[10]),
          (obj.monthValue12 = res[11]);
      }
      (obj.softwareDevelopmentProjectId =
        this.lst[i].softwareDevelopmentProjectId === undefined ? 0 : this.lst[i].softwareDevelopmentProjectId), // id kế hoạch thuê ngoài phất triển phần mềm
        (obj.outsourcePlanId = this.lst[i].softwareDevelopmentProjectId === undefined ? 0 : this.lst[i].softwareDevelopmentProjectId), //  id kế hoạch thuê ngoài
        (obj.numberContract = this.lst[i].numContract === undefined ? '' : this.lst[i].numContract), //  Số HD
        // (obj.outsourcingOrganizationId = this.lst[i].outsourcingOrganizationId), //   id Đơn vị thuê ngoài
        (obj.outsourcingOrganizationName =
          this.lst[i].outsourcingOrganizationName === undefined ? '' : this.lst[i].outsourcingOrganizationName), //   Đơn vị thuê ngoài
        (obj.proposedCooperationClueUserId =
          this.lst[i].proposedCooperationClueUserId === undefined ? '' : this.lst[i].proposedCooperationClueUserId), //  đầu mối đề nghị hợp tác Id
        (obj.numberTTrPAKD = this.lst[i].numTTrPakd === undefined ? '' : this.lst[i].numTTrPakd), //  Số TTr PAKD
        (obj.estimateEffortStatus = this.lst[i].estimateEffortStatus === undefined ? '' : this.lst[i].estimateEffortStatus), //  Trạng thái ULNL
        // update start 16/06/2020
        (obj.businessOrganizationId = this.lst[i].businessOrganizationId === undefined ? 0 : this.lst[i].businessOrganizationId), //  Đơn vị kinh doanh
        (obj.businessClueUserId = this.lst[i].businessClueUserId === undefined ? 0 : this.lst[i].businessClueUserId), //  Đầu mối kinh doanh
        (obj.manufacturingOrganizationId =
          this.lst[i].manufacturingOrganizationId === undefined ? 0 : this.lst[i].manufacturingOrganizationId), //  id Đơn vị sản xuất
        (obj.technicalClueUserId = this.lst[i].technicalClueUserId === undefined ? 0 : this.lst[i].technicalClueUserId), //  đầu mối kỹ thuật
        (obj.businessOrgName = this.lst[i].businessOrgName === undefined ? '' : this.lst[i].businessOrgName), //  Đơn vị kinh doanh
        (obj.amName = this.lst[i].amName === undefined ? '' : this.lst[i].amName), //  Đầu mối kinh doanh
        (obj.productionOrgName = this.lst[i].productionOrgName === undefined ? '' : this.lst[i].productionOrgName), //  Đơn vị sản xuất
        (obj.pmName = this.lst[i].pmName === undefined ? '' : this.lst[i].pmName), //  đầu mối kỹ thuật
        // update end 16/06/2020

        (obj.cooperationYear = this.lst[i].cooperationYear === undefined ? 0 : this.lst[i].cooperationYear); //  năm hợp tác
      obj.totalAppraisedMM = this.lst[i].totalAppraisedMM === undefined ? '' : this.lst[i].totalAppraisedMM; //  Tổng MM đã thẩm định

      const numTotalAppraisedMM =
        this.lst[i].totalAppraisedMM === undefined || this.lst[i].totalAppraisedMM === '' ? 0 : this.lst[i].totalAppraisedMM;
      const numTotalUsedMM = this.lst[i].totalUsedMM === undefined || this.lst[i].totalUsedMM === '' ? 0 : this.lst[i].totalUsedMM;

      (obj.totalSaveMM = Number(numTotalAppraisedMM) - Number(numTotalUsedMM)), //  MM còn lại
        (obj.totalRealityMM = this.lst[i].totalUsedMM === undefined ? 0 : this.lst[i].totalUsedMM); //  MM sử dụng thực tế
      obj.id = this.lst[i].id === undefined ? 0 : this.lst[i].id; // id
      obj.projectId = this.lst[i].projectId === undefined ? 0 : this.lst[i].projectId;
      obj.projectCode = this.lst[i].projectCode === undefined ? 0 : this.lst[i].projectCode;
      obj.projectName = this.lst[i].projectName === undefined ? '' : this.lst[i].projectName;
      obj.planCode = this.lst[i].planCode === undefined ? 0 : this.lst[i].planCode;
      if (this.form.value.type === 'create') {
        obj.status = false;
      } else {
        obj.status = true;
        obj.flagOutSourcingPlan = true;
        obj.flagMMOS = true;
      }

      const dataDefaultDMDNHT = {
        id: this.lst[i].proposedCooperationClueUserId,
        name: this.lst[i].nameUser,
        mail: this.lst[i].mailUser,
        code: this.lst[i].codeUser
      };

      const dataDefaultProject = {
        id: this.lst[i].projectId,
        projectCode: this.lst[i].projectCode,
        projectName: this.lst[i].projectName
      };

      const dataDefaultDMKD = {
        id: this.lst[i].businessClueUserId,
        name: this.lst[i].amName
      };

      const dataDefaultDMKT = {
        id: this.lst[i].technicalClueUserId,
        name: this.lst[i].pmName
      };

      //  call dự án
      obj.listUnit$ = of([dataDefaultProject]);
      //  call lại đầu mối đề nghị hợp tác
      obj.listDMDVHT$ = of([dataDefaultDMDNHT]);
      // call lại đầu mối kĩ thuật
      obj.listDMKT$ = of([dataDefaultDMKT]);
      // call lại đầu mối kinh doanh
      obj.listDMKD$ = of([dataDefaultDMKD]);

      this.params.actualEffortList.push(obj);
      // this.params.actualEffortList2.push(obj);
    }

    this.numRecord = this.params.actualEffortList.length;
    // this.numRecord = this.params.actualEffortList2.length;
    this.arrPage();
    this.loadList();
  }

  loadDataOnSearchUnit2(term) {
    const words = term.split(';');
    const wordSearch = words[0];
    const index = words[1];
    this.contractManagerService.doSearchEmailOrName(wordSearch).subscribe((res: HttpResponse<any[]>) => {
      if (res['data'].length > 0) {
        this.params.actualEffortList[index].listDMDVHT$ = of(res['data']);
        this.params.actualEffortList2[index].listDMDVHT$ = of(res['data']);
      } else {
        this.params.actualEffortList[index].listDMDVHT$ = of([]);
        this.params.actualEffortList2[index].listDMDVHT$ = of([]);
      }
    });
  }

  loadDataOnSearchUnit1(term) {
    const words = term.split(';');
    const wordSearch = words[0];
    const index = words[1];
    //  search cho mã dự án
    this.contractManagerService.getAllProjectCode(wordSearch).subscribe((res: HttpResponse<any[]>) => {
      if (res['data'].length > 0) {
        this.params.actualEffortList[index].listUnit$ = of(res['data']);
        this.params.actualEffortList2[index].listUnit$ = of(res['data']);
      } else {
        this.params.actualEffortList[index].listUnit$ = of([]);
        this.params.actualEffortList2[index].listUnit$ = of([]);
      }
    });
  }

  // trangnc
  replaceSeparate(value) {
    const temp = String(value).replace(this.regexReplaceSeparate, '');
    return Number(temp.replace(this.regexReplacePoint, '.').trim());
  }

  //  trangnc thực hiện đổ data tới form (khi người dùng click xem)
  private pushDataToForm(res: any, action: String) {
    this.form.get('id').setValue(res.id); //  id bản ghi
    this.form.get('partnerId').setValue(res.partnerId); //  id đối tác
    this.form.get('partnerCode').setValue(res.partnerCode); //  mã đối tác
    this.form.get('signatureStatus').setValue(res.signatureStatus); //  trạng thái
    this.form.get('ttrpakdNumber').setValue(res.ttrpakdNumber); //  số TTrParkd
    this.form.get('contractStatus').setValue(res.contractStatus); //  trạng thái hợp đồng
    this.form.get('contractType').setValue(res.contractType); //  loại hợp đồng
    this.form.get('signDate').setValue(res.createDate); //  ngày ký hợp đồng
    this.form.get('startTime').setValue(res.startTime); //  thời gian bắt đầu
    this.form.get('endTime').setValue(res.endTime); //  thời gian kết thúc
    this.form.get('totalMM').setValue(res.totalMM); //  tổng MM thuê ngoài
    this.form.get('totalMMPayed').setValue(res.totalMMPayed); //  Tổng MM đã thanh toán
    //  objView.ownedAmount = res.ownedAmount, //   Số tiền còn nợ (tự tính) totalOwnedMM
    this.totalOwnedMM = Number(res.totalMM) - Number(res.totalMMPayed); //  MM còn nợ (MM thuê ngoài - MM đã thanh toán)
    this.form.get('totalOwnedMM').setValue(Number(res.totalMM) - Number(res.totalMMPayed)); //  MM còn nợ (MM thuê ngoài - MM đã thanh toán)
    // this.price = res.price;
    this.form.get('totalAccumulatedMM').setValue(res.mmSumLK); //  Tổng MM sử dụng luỹ kế
    this.form.get('price').setValue(res.price); //  đơn giá
    this.ownedAmount = (Number(res.totalMM) - Number(res.totalMMPayed)) * Number(res.price);
    this.form.get('ownedAmount').setValue((Number(res.totalMM) - Number(res.totalMMPayed)) * Number(res.price)); //  Số tiền còn nợ (MM còn nợ * đơn giá)
    this.form.get('contractValue').setValue(res.contractValue); //  giá trị hợp đồng
    this.contractValue = res.contractValue; //  giá trị hợp đồng
    this.form.get('contractDescription').setValue(res.getContractDescription); //  thông tin chung hợp đồng
    this.form.get('note').setValue(res.note); //  ghi chú
    this.form.get('type').setValue('update');
    //  partnerName
    const dataDefault = {
      id: res.partnerId, //  id đối tác
      partnerCode: res.partnerCode, //  mã đối tác
      partnerName: res.partnerName //  tên đối tác
    };
    this.listDT$ = of([dataDefault]);
    // ẩn các trường nếu là view
    if (action === 'view') {
      this.form.get('partnerId').disable(); //  mã dự án
      this.form.get('signatureStatus').disable(); //  trạng thái
      this.form.get('ttrpakdNumber').disable(); //  số TTrParkd
      this.form.get('contractStatus').disable(); //  trạng thái hợp đồng
      this.form.get('contractType').disable(); //  loại hợp đồng
      this.hidden = true;
      this.form.get('totalMM').disable();
      this.form.get('totalMMPayed').disable();
      this.form.get('totalOwnedMM').disable(); //  MM còn nợ
      this.form.get('price').disable(); //  đơn giá
      this.form.get('ownedAmount').disable(); //  Số tiền còn nợ
      this.form.get('contractValue').disable(); //  giá trị hợp đồng
      this.form.get('contractDescription').disable(); //  thông tin chung hợp đồng
      this.form.get('note').disable(); //  ghi chú
      this.form.get('totalAccumulatedMM').disable();
    } else {
      //  cho phép sửa nếu là update
      this.form.get('partnerId').enable(); //  mã dự án
      this.form.get('signatureStatus').enable(); //  trạng thái
      this.form.get('ttrpakdNumber').enable(); //  số TTrParkd
      this.form.get('contractStatus').enable(); //  trạng thái hợp đồng
      this.form.get('contractType').enable(); //  loại hợp đồng
      this.hidden = false;
      this.form.get('totalMM').enable();
      this.form.get('totalMMPayed').enable();
      this.form.get('totalOwnedMM').enable(); //  MM còn nợ
      this.form.get('price').enable(); //  đơn giá
      this.form.get('ownedAmount').enable(); //  Số tiền còn nợ
      this.form.get('contractValue').enable(); //  giá trị hợp đồng
      this.form.get('contractDescription').enable(); //  thông tin chung hợp đồng
      this.form.get('note').enable(); //  ghi chú
      this.form.get('totalAccumulatedMM').enable();
    }
  }

  //  trangnc
  getDate(value) {
    return new Date(value).getTime();
  }

  //  trangnc
  onResize() {
    this.height = this.heightService.onResizeWithoutFooter();
  }

  isNotEmpty(value) {
    return value !== undefined && value !== null && value !== '';
  }

  onSum(effort) {
    effort.totalMD = 0;
    effort.monthValues = '';
    effort.monthList.forEach((mo: any) => {
      effort.totalMD += this.isNotEmpty(mo.m) ? mo.m : 0;
      effort.monthValues += mo.m + ';';
    });
    effort.monthValues = effort.monthValues.substr(0, effort.monthValues.length - 1);
    effort.totalUsedMM = Math.round((effort.totalMD ? effort.totalMD / 22 : 0) * 100) / 100;
  }

  // //  trangnc
  // onChangeDate(flag?: any) {
  //   // this.setDefaultValue();
  //   const value = this.form.value;
  //   const endTime = value.endTime;
  //   const startTime = value.startTime;
  //   if (endTime !== '' && startTime !== '') {
  //     if (this.getDate(startTime) - this.getDate(endTime) > 0) {
  //       if (flag === 1) {
  //         this.error = 'startTime';
  //         this.message = 'Thời gian bắt đầu đang lớn hơn thời gian kết thúc';
  //       } else if (flag === 2) {
  //         this.error = 'endTime';
  //         this.message = 'Thời gian kết thúc đang nhỏ hơn thời gian bắt đầu';
  //       }
  //     } else {
  //       this.message = '';
  //     }
  //   }
  // }

  checkDate() {
    this.messageDate = '';
    const value = this.form.value;
    const endTime = value.endTime;
    const startTime = value.startTime;
    if (endTime !== '' && startTime !== '' && endTime !== undefined && startTime !== undefined && endTime !== null && startTime !== null) {
      if (this.getDate(value.startTime) - this.getDate(value.endTime) > 0) {
        this.messageDate = 'Thời gian bắt đầu đang lớn hơn thời gian kết thúc';
        //this.buttonDisable = true;
        return 1;
      } else {
        this.messageDate = '';
        //this.buttonDisable = false;
      }
    }
  }
  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;
  }

  get formControl() {
    return this.form.controls;
  }
  checkDate1() {
    this.message = '';
    const value = this.form.value;
    const endDate = value.endDate;
    const startDate = value.startDate;
    if (endDate !== '' && startDate !== '') {
      if (this.getDate(value.startDate) - this.getDate(value.endDate) > 0) {
        this.message = 'Ngày tham gia đang lớn hơn kết thúc';
        return 1;
      } else {
        this.message = '';
      }
    }
  }
  transition() {
    const formValue = this.form.value;
    //  this.doSearch();
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
  // check MaxLength
  checkMaxLenght(event, length) {
    const size = event.target.value.toString().length;
    if (size < Number(length - 1)) {
      return true;
    } else {
      return false;
    }
  }

  //  trangnc code
  onInputChange(event) {
    // update start 16/06/2020
    this.clErrorTtal = '';
    this.error = '';
    this.message = '';
    // update end 16/06/2020
    //  kiểm tra chuoi có dau '.'
    const value = event.target.value;
    if (value.toString().includes('.') && event.key === '.') {
      //  không cho phép nhập 2 dấu chấm
      return false;
    } else if (value === '' && event.key === '.') {
      //  không cho phép nhập '.' khi bắt đầu
      return false;
    } else {
      //  không cho phép chữ
      const charCode = event.which ? event.which : event.keyCode;
      if (charCode === 46) {
        return true;
      } else if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
      } else {
        return true;
      }
    }
  }

  onInputChangeMM(event) {
    const value = event.target.value;
    // không cho nhập số 0 đầu tiên
    if (value === '' && event.key === '0') {
      return false;
    }
    if (value.toString().includes('.') && event.key === '.') {
      //  không cho phép nhập 2 dấu chấm
      return false;
    } else if (value === '' && event.key === '.') {
      //  không cho phép nhập '.' khi bắt đầu
      return false;
    } else {
      //  không cho phép chữ
      const charCode = event.which ? event.which : event.keyCode;
      if (charCode === 46) {
        return true;
      } else if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
      } else return true;
    }
  }

  onInputChangeMM2(event, length) {
    const size = event.target.value.toString().length;
    if (size > Number(length)) {
      //check lenght
      return false;
    }
    const value = event.target.value;
    // không cho nhập số 0 đầu tiên
    if (value === '' && event.key === '0') {
      return false;
    }
    if (value.toString().includes('.') && event.key === '.') {
      //  không cho phép nhập 2 dấu chấm
      return false;
    } else if (value === '' && event.key === '.') {
      //  không cho phép nhập '.' khi bắt đầu
      return false;
    } else {
      //  không cho phép chữ
      const charCode = event.which ? event.which : event.keyCode;
      if (charCode === 46) {
        return true;
      } else if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
      } else return true;
    }
  }

  //
  validateTotal() {
    const sizeTotal = this.form.value.totalMM.length;
    if (sizeTotal <= 0) {
      this.clErrorTtal = 'has-error';
      this.errorMMTN = 'totalMM';
      this.message = 'Tổng MM thuê ngoài là bắt buộc';
    } else {
      this.clErrorTtal = '';
      this.errorMMTN = '';
      this.message = '';
    }
  }

  // trangnc
  onChangeTotalMD(index: any) {
    //  tổng MD
    //  get value months
    const valM1 =
      this.params.actualEffortList[index].monthValue1 === '' || this.params.actualEffortList[index].monthValue1 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue1;
    const valM2 =
      this.params.actualEffortList[index].monthValue2 === '' || this.params.actualEffortList[index].monthValue2 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue2;
    const valM3 =
      this.params.actualEffortList[index].monthValue3 === '' || this.params.actualEffortList[index].monthValue3 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue3;
    const valM4 =
      this.params.actualEffortList[index].monthValue4 === '' || this.params.actualEffortList[index].monthValue4 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue4;
    const valM5 =
      this.params.actualEffortList[index].monthValue5 === '' || this.params.actualEffortList[index].monthValue5 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue5;
    const valM6 =
      this.params.actualEffortList[index].monthValue6 === '' || this.params.actualEffortList[index].monthValue6 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue6;
    const valM7 =
      this.params.actualEffortList[index].monthValue7 === '' || this.params.actualEffortList[index].monthValue7 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue7;
    const valM8 =
      this.params.actualEffortList[index].monthValue8 === '' || this.params.actualEffortList[index].monthValue8 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue8;
    const valM9 =
      this.params.actualEffortList[index].monthValue9 === '' || this.params.actualEffortList[index].monthValue9 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue9;
    const valM10 =
      this.params.actualEffortList[index].monthValue10 === '' || this.params.actualEffortList[index].monthValue10 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue10;
    const valM11 =
      this.params.actualEffortList[index].monthValue11 === '' || this.params.actualEffortList[index].monthValue11 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue11;
    const valM12 =
      this.params.actualEffortList[index].monthValue12 === '' || this.params.actualEffortList[index].monthValue12 === undefined
        ? 0
        : this.params.actualEffortList[index].monthValue12;

    const sumMD =
      Number(valM1) +
      Number(valM2) +
      Number(valM3) +
      Number(valM4) +
      Number(valM5) +
      Number(valM6) +
      Number(valM7) +
      Number(valM8) +
      Number(valM9) +
      Number(valM10) +
      Number(valM11) +
      Number(valM12);

    const valtotalAppraisedMM =
      this.params.actualEffortList[index].totalAppraisedMM === '' || this.params.actualEffortList[index].totalAppraisedMM === undefined
        ? 0
        : this.params.actualEffortList[index].totalAppraisedMM;

    this.params.actualEffortList[index].totalRealityMD = sumMD;
    this.params.actualEffortList[index].totalRealityMM = sumMD / 22;
    this.params.actualEffortList[index].totalSaveMM = Number(valtotalAppraisedMM) - sumMD / 22;
    //
    const length = this.params.actualEffortList.length;
    let total = 0;
    let totalLK = 0;
    for (let i = 0; i < length; i++) {
      total += Number(this.params.actualEffortList[i].totalAppraisedMM);
      totalLK += this.params.actualEffortList[i].totalRealityMM;
    }

    this.form.get('totalMM').setValue(total);
    this.form.get('totalAccumulatedMM').setValue(totalLK);
    this.onChangeTotal();
  }

  //  trangnc
  setDecimal() {
    this.totalMM.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.totalMM.decimalSymbol = this.decimalPointSpace;

    //  this.price.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.price.decimalSymbol = this.decimalPointSpace;
    //
    this.contractVal.decimalSymbol = this.decimalPointSpace;
    this.totalAppraisedMM.thousandsSeparatorSymbol = this.decimalPointSignSeparate;

    this.totalAccumulatedMM.decimalSymbol = this.decimalPointSpace;

    this.totalMMPayed.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.totalMMPayed.decimalSymbol = this.decimalPointSpace;

    this.totalAppraisedMM.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.totalAppraisedMM.decimalSymbol = this.decimalPointSpace;
    //  tháng 1 MM
    this.monthValue1.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue1.decimalSymbol = this.decimalPointSpace;
    //  tháng 2 MM
    this.monthValue2.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue2.decimalSymbol = this.decimalPointSpace;
    //  tháng 3 MM
    this.monthValue3.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue3.decimalSymbol = this.decimalPointSpace;
    //  tháng 4 MM
    this.monthValue4.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue4.decimalSymbol = this.decimalPointSpace;
    //  tháng 5 MM
    this.monthValue5.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue5.decimalSymbol = this.decimalPointSpace;
    //  tháng 6 MM
    this.monthValue6.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue6.decimalSymbol = this.decimalPointSpace;
    //  tháng 7 MM
    this.monthValue7.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue7.decimalSymbol = this.decimalPointSpace;
    //  tháng 8 MM
    this.monthValue8.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue8.decimalSymbol = this.decimalPointSpace;
    //  tháng 9 MM
    this.monthValue9.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue9.decimalSymbol = this.decimalPointSpace;
    //  tháng 10 MM
    this.monthValue10.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue10.decimalSymbol = this.decimalPointSpace;
    //  tháng 11 MM
    this.monthValue11.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue11.decimalSymbol = this.decimalPointSpace;
    //  tháng 12 MM
    this.monthValue12.thousandsSeparatorSymbol = this.decimalPointSignSeparate;
    this.monthValue12.decimalSymbol = this.decimalPointSpace;

    this.regexReplaceSeparate = new RegExp('[' + this.decimalPointSignSeparate + ']*', 'g'); //  phan nghin
    this.regexReplacePoint = new RegExp('[' + this.decimalPointSpace + ']', 'g'); //  thap phan
    this.createMask();
  }

  createMask() {
    this.totalMM = createNumberMask({ ...this.totalMM });
    this.price = createNumberMask({ ...this.price });
    //
    this.contractVal = createNumberMask({ ...this.contractVal });

    this.totalAccumulatedMM = createNumberMask({ ...this.totalAccumulatedMM });
    this.totalMMPayed = createNumberMask({ ...this.totalMMPayed });
    this.totalAppraisedMM = createNumberMask({ ...this.totalAppraisedMM });
    //  tháng 1 MM
    this.monthValue1 = createNumberMask({ ...this.monthValue1 });
    //  tháng 2 MM
    this.monthValue2 = createNumberMask({ ...this.monthValue2 });
    //  tháng 3 MM
    this.monthValue3 = createNumberMask({ ...this.monthValue3 });
    //  tháng 4 MM
    this.monthValue4 = createNumberMask({ ...this.monthValue4 });
    //  tháng 5 MM
    this.monthValue5 = createNumberMask({ ...this.monthValue5 });
    //  tháng 6 MM
    this.monthValue6 = createNumberMask({ ...this.monthValue6 });
    //  tháng 7 MM
    this.monthValue7 = createNumberMask({ ...this.monthValue7 });
    //  tháng 8 MM
    this.monthValue8 = createNumberMask({ ...this.monthValue8 });
    //  tháng 9 MM
    this.monthValue9 = createNumberMask({ ...this.monthValue9 });
    //  tháng 10 MM
    this.monthValue10 = createNumberMask({ ...this.monthValue10 });
    //  tháng 11 MM
    this.monthValue11 = createNumberMask({ ...this.monthValue11 });
    //  tháng 12 MM
    this.monthValue12 = createNumberMask({ ...this.monthValue12 });
  }

  getValueOfField(item) {
    if (
      this.form.get(item).value === '' ||
      this.form.get(item).value === null ||
      this.form.get(item).value === 'undefined' ||
      this.form.get(item).value === undefined
    ) {
      return 0;
    } else {
      return this.form.get(item).value;
    }
  }

  //  trangnc
  onChangeTotal() {
    this.totalOwnedMM = this.getValueOfField('totalMM') - this.getValueOfField('totalMMPayed'); // MM còn nợ = Tổng MM thuê ngoài - Tổng MM đã thanh toán
    //  validate •	Giá trị hợp đồng
    if (this.form.get('totalMM').value === '' && this.form.get('price').value === '') {
      this.form.controls['contractValue'].enable();
    } else if (this.form.get('totalMM').value !== '' || this.form.get('price').value !== '') {
      this.form.controls['contractValue'].disable();
    }

    if (
      this.getValueOfField('price')
        .toString()
        .includes(',') ||
      this.getValueOfField('totalMM')
        .toString()
        .includes(',')
    ) {
      const price = this.validateNumber(this.getValueOfField('price'));
      const totalMM = this.validateNumber(this.getValueOfField('totalMM'));
      this.contractValue = Number(price) * Number(totalMM);
    } else {
      this.contractValue = this.getValueOfField('price') * this.getValueOfField('totalMM');
    }

    //  validate •	Số tiền còn nợ
    // if (this.form.get('price').value === 0 && this.form.get('totalOwnedMM').value === 0) {
    //   this.form.controls['ownedAmount'].enable();
    // } else

    if (this.getValueOfField('price') !== 0 && this.getValueOfField('totalOwnedMM') !== 0) {
      this.form.controls['ownedAmount'].disable();
    } else {
      this.form.controls['ownedAmount'].enable();
    }

    if (
      this.getValueOfField('price')
        .toString()
        .includes(',')
    ) {
      const price = this.validateNumber(this.getValueOfField('price'));
      this.ownedAmount = Number(price) * (this.totalOwnedMM === undefined || this.totalOwnedMM === '' ? 0 : this.totalOwnedMM);
    } else {
      // this.ownedAmount = this.getValueOfField('price') * this.getValueOfField('totalOwnedMM');   // this.totalOwnedMM
      this.ownedAmount =
        this.getValueOfField('price') * (this.totalOwnedMM === undefined || this.totalOwnedMM === '' ? 0 : this.totalOwnedMM);
    }
  }

  //  trangnc xóa dấu phẩy
  validateNumber(data) {
    if (data.toString().includes(',')) {
      while (data.toString().includes(',')) {
        data = data.toString().replace(',', '');
      }
      return Number(data.toString().replace(',', ''));
    } else {
      return Number(data);
    }
  }

  //  validate Tổng MM thuê ngoài - end

  onChangeDatalist(item, effortIndex, value) {
    this.params.actualEffortList[effortIndex][item] = this.dropdownDataMap[value] ? this.dropdownDataMap[value] : null;
    //  this.setDefaultValue();
  }

  // validDate(i) {
  //   let checkValid = true;
  //   if (this.lstHuman[i].roleId==='' || this.lstHuman[i].humanResourceId==='') {
  //     checkValid = false;
  //   }
  //   return checkValid;
  // }

  onCloseAddModal() {
    console.log(this.type);
    this.router.navigate(['system-categories/project-management']);
    // if (this.type !== 'detail') {
    //   this.isModalConfirmShow = true;
    //   const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
    //   modalRef.componentInstance.type = 'confirm';
    //   modalRef.componentInstance.onCloseModal.subscribe(value => {
    //     if (value === true) {
    //       this.router.navigate(['system-categories/project-management']);
    //     }
    //     this.isModalConfirmShow = false;
    //   });
    // } else {
    //   // this.activeModal.dismiss();
    //   this.form.reset();
    //   // this.activeModal.dismiss(true);
    // }
  }
  onDeleteEffort(index) {
    const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
    const idDelete = this.params.actualEffortList[index].id;
    modalRef.componentInstance.type = 'delete';
    modalRef.componentInstance.param = this.translateService.instant('actualEffort.title-delete');
    modalRef.componentInstance.onCloseModal.subscribe(value => {
      if (value === true) {
        if (idDelete !== 'undefined' || idDelete !== undefined) {
          this.objDelete = JSON.parse(JSON.stringify(this.params.actualEffortList[index]));
          this.objDeleteDB = this.converObj();
          this.lstdataDelete.push(this.objDeleteDB);
          this.objDelete = {};
          this.objDeleteDB = {};
        }
        this.params.actualEffortList.splice(index, 1);
        this.toastService.openSuccessToast(
          this.translateService.instant('common.toastr.messages.success.delete', {
            paramName: this.translateService.instant('actualEffort.title')
          })
        );
      }
    });
  }

  converObj() {
    const objDeleteDB: any = {};
    objDeleteDB.id = this.objDelete.id; //  id
    objDeleteDB.softwareDevelopmentProjectId = this.objDelete.softwareDevelopmentProjectId; //  Mã dự án
    objDeleteDB.outsourcePlanId = this.objDelete.outsourcePlanId; //  Kế hoạch thuê ngoài
    objDeleteDB.numberContract = this.objDelete.numberContract; //  Số HD
    objDeleteDB.outsourcingOrganizationName = this.objDelete.outsourcingOrganizationName; //   Đơn vị thuê ngoài
    objDeleteDB.proposedCooperationClueUserId = this.objDelete.proposedCooperationClueUserId; //  đầu mối đề nghị hợp tác
    objDeleteDB.numberTTrPAKD = this.objDelete.numberTTrPAKD; //  Số TTr PAKD
    objDeleteDB.estimateEffortStatus = this.objDelete.estimateEffortStatus; //  Trạng thái ULNL
    objDeleteDB.businessOrganizationId = this.objDelete.businessOrganizationId; //  Đơn vị kinh doanh
    objDeleteDB.businessClueUserId = this.objDelete.businessClueUserId; //  Đầu mối kinh doanh
    objDeleteDB.manufacturingOrganizationId = this.objDelete.manufacturingOrganizationId; //  Đơn vị sản xuất
    objDeleteDB.technicalClueUserId = this.objDelete.technicalClueUserId; //  đầu mối kỹ thuật
    objDeleteDB.cooperationYear = this.objDelete.cooperationYear; //  năm hợp tác
    objDeleteDB.totalAppraisedMM = this.objDelete.totalAppraisedMM; //   Tổng MM đã thẩm định
    objDeleteDB.totalSaveMM = this.objDelete.totalSaveMM; //  MM còn lại
    objDeleteDB.totalUsedMM = this.objDelete.totalRealityMM; //  MM sử dụng thực tế
    objDeleteDB.outsourcingContractId = 1; //  ID Hợp đồng thuê ngoài
    objDeleteDB.projectId = this.objDelete.projectId; //  id dự án
    objDeleteDB.isActive = 0;

    objDeleteDB.projectCode = this.objDelete.projectCode;
    objDeleteDB.projectName = this.objDelete.projectName;
    objDeleteDB.nameUser = this.objDelete.nameUser;
    objDeleteDB.mailUser = this.objDelete.mailUser;
    objDeleteDB.codeUser = this.objDelete.codeUser;
    objDeleteDB.planCode = this.objDelete.planCode;
    //  month value
    let totalMD = '';
    totalMD += ';' + this.objDelete.monthValue1;
    totalMD += ';' + this.objDelete.monthValue2;
    totalMD += ';' + this.objDelete.monthValue3;
    totalMD += ';' + this.objDelete.monthValue4;
    totalMD += ';' + this.objDelete.monthValue5;
    totalMD += ';' + this.objDelete.monthValue6;
    totalMD += ';' + this.objDelete.monthValue7;
    totalMD += ';' + this.objDelete.monthValue8;
    totalMD += ';' + this.objDelete.monthValue9;
    totalMD += ';' + this.objDelete.monthValue10;
    totalMD += ';' + this.objDelete.monthValue11;
    totalMD += ';' + this.objDelete.monthValue12;
    objDeleteDB.monthValues = totalMD;

    objDeleteDB.numContract = this.objDelete.numberContract; //  số HĐ
    objDeleteDB.numTTrPakd = this.objDelete.numberTTrPAKD; //  TTrPard

    objDeleteDB.listDMDVHT$ = this.objDelete.listDMDVHT$;
    objDeleteDB.listUnit$ = this.objDelete.listUnit$;

    objDeleteDB.status = true;
    return objDeleteDB;
  }

  onSubmitDelete(id?: any) {
    this.spinner.show();
  }



  doNotUpdateEffort(effortIndex?: number) {
    if (this.objClone.softwareDevelopmentProjectId === undefined) {
      this.params.actualEffortList.splice(effortIndex, 1);
      this.params.actualEffortList2.splice(effortIndex, 1);
      this.buttonDisabled = false;
    } else {
      this.params.actualEffortList[effortIndex].softwareDevelopmentProjectId = this.objClone.softwareDevelopmentProjectId; //  Mã dự án
      this.params.actualEffortList[effortIndex].outsourcePlanId = this.objClone.outsourcePlanId; //  Kế hoạch thuê ngoài
      this.params.actualEffortList[effortIndex].numberContract = this.objClone.numberContract; //  Số HD
      this.params.actualEffortList[effortIndex].outsourcingOrganizationName = this.objClone.outsourcingOrganizationName; //   Đơn vị thuê ngoài
      this.params.actualEffortList[effortIndex].proposedCooperationClueUserId = this.objClone.proposedCooperationClueUserId; //  đầu mối đề nghị hợp tác
      this.params.actualEffortList[effortIndex].numberTTrPAKD = this.objClone.numberTTrPAKD; //  Số TTr PAKD
      this.params.actualEffortList[effortIndex].estimateEffortStatus = this.objClone.estimateEffortStatus; //  Trạng thái ULNL
      this.params.actualEffortList[effortIndex].businessOrganizationId = this.objClone.businessOrganizationId; //  Đơn vị kinh doanh
      this.params.actualEffortList[effortIndex].businessClueUserId = this.objClone.businessClueUserId; //  Đầu mối kinh doanh
      this.params.actualEffortList[effortIndex].manufacturingOrganizationId = this.objClone.manufacturingOrganizationId; //  Đơn vị sản xuất
      this.params.actualEffortList[effortIndex].technicalClueUserId = this.objClone.technicalClueUserId; //  đầu mối kỹ thuật
      this.params.actualEffortList[effortIndex].cooperationYear = this.objClone.cooperationYear; //  năm hợp tác
      this.params.actualEffortList[effortIndex].totalAppraisedMM = this.objClone.totalAppraisedMM; //   Tổng MM đã thẩm định
      this.params.actualEffortList[effortIndex].totalSaveMM = this.objClone.totalSaveMM; //  MM còn lại
      this.params.actualEffortList[effortIndex].totalRealityMM = this.objClone.totalRealityMM; //  MM sử dụng thực tế
      this.params.actualEffortList[effortIndex].projectCode = this.objClone.projectCode;
      this.params.actualEffortList[effortIndex].projectName = this.objClone.projectName;
      this.params.actualEffortList[effortIndex].nameUser = this.objClone.nameUser;
      this.params.actualEffortList[effortIndex].mailUser = this.objClone.mailUser;
      this.params.actualEffortList[effortIndex].codeUser = this.objClone.codeUser;
      this.params.actualEffortList[effortIndex].planCode = this.objClone.planCode;
      //  month value
      this.params.actualEffortList[effortIndex].monthValue1 = this.objClone.monthValue1;
      this.params.actualEffortList[effortIndex].monthValue2 = this.objClone.monthValue2;
      this.params.actualEffortList[effortIndex].monthValue3 = this.objClone.monthValue3;
      this.params.actualEffortList[effortIndex].monthValue4 = this.objClone.monthValue4;
      this.params.actualEffortList[effortIndex].monthValue5 = this.objClone.monthValue5;
      this.params.actualEffortList[effortIndex].monthValue6 = this.objClone.monthValue6;
      this.params.actualEffortList[effortIndex].monthValue7 = this.objClone.monthValue7;
      this.params.actualEffortList[effortIndex].monthValue8 = this.objClone.monthValue8;
      this.params.actualEffortList[effortIndex].monthValue9 = this.objClone.monthValue9;
      this.params.actualEffortList[effortIndex].monthValue10 = this.objClone.monthValue10;
      this.params.actualEffortList[effortIndex].monthValue11 = this.objClone.monthValue11;
      this.params.actualEffortList[effortIndex].monthValue12 = this.objClone.monthValue12;
      this.params.actualEffortList[effortIndex].listDMDVHT$ = this.objClone.listDMDVHT$;
      this.params.actualEffortList[effortIndex].listUnit$ = this.objClone.listUnit$;
      //  call lại đầu mối đề nghị hợp tác
      //  this.loadDataOnSearchUnit2(this.objClone.nameUser + ';' + effortIndex);
      //  call lại dự án
      // this.loadDataOnSearchUnit1(this.objClone.projectCode + ';' + effortIndex);
      this.params.actualEffortList[effortIndex].status = true;
      this.buttonDisabled = false;
      this.objClone = {};
    }
  }




  doAdd(val?: any, action?: String) {
    //  thực hiện add
    this.objectInsert.id = val.id;
    this.objectInsert.createDate = new Date();
    this.objectInsert.updateDate = new Date();
    this.objectInsert.createUser = 1;
    this.objectInsert.userModifyId = 1;
    this.objectInsert.isActive = 1;
    this.objectInsert.partnerId = val.partnerId; //  id đối tác
    this.objectInsert.signatureStatus = val.signatureStatus; //  trạng thái ký
    this.objectInsert.contractStatus = val.contractStatus; //  trạng thái hợp đồng
    this.objectInsert.contractType = val.contractType; //  loại hợp đồng
    this.objectInsert.startTime = val.startTime === '' ? '' : this.convertDate(val.startTime); //  thời gian bắt đầu
    this.objectInsert.endTime = val.startTime === '' ? '' : this.convertDate(val.endTime); //  thời gian kết thúc
    this.objectInsert.totalMM = val.totalMM; //  tổng MM
    this.objectInsert.totalMMPayed = val.totalMMPayed; //  tổng MM đã thanh toán
    const price2Num = this.validateNumber(val.price);
    this.objectInsert.price = price2Num; //  giá
    const contractVNumber = this.validateNumber(this.contractValue);
    this.objectInsert.contractValue = contractVNumber; //  giá trị hợp đồng
    this.objectInsert.amountOwed = val.ownedAmount === '' || val.ownedAmount === undefined ? 0 : val.ownedAmount; // Số tiền còn nợ

    this.objectInsert.getContractDescription = val.contractDescription; //  thông tin chung
    this.objectInsert.note = val.note; //  miêu tả
    const paramsActualEffortListLength = this.params.actualEffortList.length;

    if (val.totalAccumulatedMM === undefined || val.totalAccumulatedMM === '') {
      for (let i = 0; i < paramsActualEffortListLength; i++) {
        this.numberLK +=
          this.params.actualEffortList[i].totalRealityMM === undefined || this.params.actualEffortList[i].totalRealityMM === ''
            ? 0
            : Number(this.params.actualEffortList[i].totalRealityMM);
      }
      this.objectInsert.mmSumLK = this.numberLK; // tổng MM lũy kế
    } else {
      this.objectInsert.mmSumLK = val.totalAccumulatedMM; // tổng MM lũy kế
    }

    //  start call api insert thong tin chung
    this.contractManagerService.doInsertData(this.objectInsert).subscribe(res => {
      if (res) {
        //  insert data thong tin no luc thuc te
        this.insertDataTTNLTT(res.id, 'add');
        this.toastService.openSuccessToast('Thêm mới thành công');
      } else {
        this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.save'));
      }
    });
  }

  doUpdateForm(val?: any) {
    //  thực hiện add
    this.objectInsert.id = val.id;
    this.objectInsert.createDate = new Date();
    this.objectInsert.updateDate = new Date();
    this.objectInsert.createUser = 1;
    this.objectInsert.userModifyId = 1;
    this.objectInsert.isActive = 1;
    this.objectInsert.partnerId = val.partnerId; //  id đối tác
    this.objectInsert.signatureStatus = val.signatureStatus; //  trạng thái ký
    this.objectInsert.contractStatus = val.contractStatus; //  trạng thái hợp đồng
    this.objectInsert.contractType = val.contractType; //  loại hợp đồng
    this.objectInsert.startTime = val.startTime === '' ? '' : this.convertDate(val.startTime); //  thời gian bắt đầu
    this.objectInsert.endTime = val.startTime === '' ? '' : this.convertDate(val.endTime); //  thời gian kết thúc
    this.objectInsert.totalMM = val.totalMM; //  tổng MM
    this.objectInsert.totalMMPayed = val.totalMMPayed; //  tổng MM đã thanh toán
    const price2Num = this.validateNumber(val.price);
    this.objectInsert.price = price2Num; //  giá
    const contractVNumber = this.validateNumber(this.contractValue);
    this.objectInsert.contractValue = contractVNumber; //  giá trị hợp đồng
    this.objectInsert.getContractDescription = val.contractDescription; //  thông tin chung
    this.objectInsert.note = val.note; //  miêu tả
    const paramsActualEffortListLength = this.params.actualEffortList.length;
    this.objectInsert.amountOwed = val.ownedAmount === '' || val.ownedAmount === undefined ? 0 : val.ownedAmount; // Số tiền còn nợ

    if (val.totalAccumulatedMM === undefined || val.totalAccumulatedMM === '') {
      for (let i = 0; i < paramsActualEffortListLength; i++) {
        this.numberLK +=
          this.params.actualEffortList[i].totalRealityMM === undefined || this.params.actualEffortList[i].totalRealityMM === ''
            ? 0
            : Number(this.params.actualEffortList[i].totalRealityMM);
      }
      this.objectInsert.mmSumLK = this.numberLK; // tổng MM lũy kế
    } else {
      this.objectInsert.mmSumLK = val.totalAccumulatedMM; // tổng MM lũy kế
    }

    //  start call api insert thong tin chung
    this.contractManagerService.doInsertData(this.objectInsert).subscribe(res => {
      if (res) {
        //  update data thong tin no luc thuc te
        this.doUpdate();
        this.toastService.openSuccessToast('Cập nhật dữ liệu thành công');
        this.onCancel(1);
      } else {
        this.toastService.openErrorToast(this.translateService.instant('common.toastr.messages.error.save'));
      }
    });
  }

  doUpdate() {
    // update những dữ liệu đã bị xóa
    const sizeLstDD = this.lstdataDelete.length;
    if (sizeLstDD > 0) {
      for (let i = 0; i < sizeLstDD; i++) {
        const element = this.lstdataDelete[i];
        this.insertDataTTNLSDTT(element, -1);
      }
    }
    // update data tới thông tin nỗ lực thực tế
    const paramsActualEffortListLength = this.params.actualEffortList.length;
    if (paramsActualEffortListLength !== 0) {
      for (let index = 0; index < paramsActualEffortListLength; index++) {
        const objectInsert2: any = {};
        objectInsert2.id = this.params.actualEffortList[index].id; // id
        objectInsert2.createDate = ''; //  CreateDate
        objectInsert2.createUser = ''; //  UserCreateId
        objectInsert2.updateDate = ''; //  updateDate
        objectInsert2.UserModifyId = ''; //  UserModifyId
        objectInsert2.isActive = 1;

        // objectInsert2.outsourcingContractId = this.form.value.outsourcingContractId; //  ID Hợp đồng thuê ngoài
        objectInsert2.outsourcingContractId = this.form.value.id; //  ID Hợp đồng thuê ngoài

        objectInsert2.projectId = this.params.actualEffortList[index].projectId; //  id dự án
        objectInsert2.softwareDevelopmentProjectId = this.params.actualEffortList[index].outsourcePlanId; //  kế hoạch thuê ngoài
        // objectInsert2.outsourcingOrganizationId = this.params.actualEffortList[index].outsourcingOrganizationId; //  đơn vị thuê ngoài
        objectInsert2.outsourcingOrganizationName = this.params.actualEffortList[index].outsourcingOrganizationName; //  đơn vị thuê ngoài
        objectInsert2.proposedCooperationClueUserId = this.params.actualEffortList[index].proposedCooperationClueUserId; //  đầu mối đề nghị hợp tác
        objectInsert2.estimateEffortStatus = this.params.actualEffortList[index].estimateEffortStatus; //  ULNL
        // update start 16/06/2020
        objectInsert2.businessOrganizationId = this.params.actualEffortList[index].businessOrganizationId; //  Đơn vị kinh doanh
        objectInsert2.businessClueUserId = this.params.actualEffortList[index].businessClueUserId; //  Đầu mối kinh doanh
        objectInsert2.manufacturingOrganizationId = this.params.actualEffortList[index].manufacturingOrganizationId; //  Đơn vị sản xuất
        objectInsert2.technicalClueUserId = this.params.actualEffortList[index].technicalClueUserId; //  Đầu mối kỹ thuật
        // -->
        (objectInsert2.businessOrgName = this.params.actualEffortList[index].businessOrgName), //  Đơn vị kinh doanh
          (objectInsert2.amName = this.params.actualEffortList[index].amName), //  Đầu mối kinh doanh
          (objectInsert2.productionOrgName = this.params.actualEffortList[index].productionOrgName), //  Đơn vị sản xuất
          (objectInsert2.pmName = this.params.actualEffortList[index].pmName), //  đầu mối kỹ thuật
          // update end 16/06/2020
          (objectInsert2.cooperationYear = this.params.actualEffortList[index].cooperationYear); //  Năm hợp tác
        objectInsert2.totalAppraisedMM = this.params.actualEffortList[index].totalAppraisedMM; //  Tổng MM đã thẩm định
        objectInsert2.totalUsedMM = this.params.actualEffortList[index].totalRealityMM; //  Tổng MM sử dụng thực tế
        let totalMD = '';
        totalMD += this.params.actualEffortList[index].monthValue1 === undefined ? '' : this.params.actualEffortList[index].monthValue1; //  Tháng 1 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue2 === undefined ? '' : this.params.actualEffortList[index].monthValue2); //  Tháng 2 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue3 === undefined ? '' : this.params.actualEffortList[index].monthValue3); //  Tháng 3 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue4 === undefined ? '' : this.params.actualEffortList[index].monthValue4); //  Tháng 4 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue5 === undefined ? '' : this.params.actualEffortList[index].monthValue5); //  Tháng 5 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue6 === undefined ? '' : this.params.actualEffortList[index].monthValue6); //  Tháng 6 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue7 === undefined ? '' : this.params.actualEffortList[index].monthValue7); //  Tháng 7 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue8 === undefined ? '' : this.params.actualEffortList[index].monthValue8); //  Tháng 8 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue9 === undefined ? '' : this.params.actualEffortList[index].monthValue9); //  Tháng 9 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue10 === undefined ? '' : this.params.actualEffortList[index].monthValue10); //  Tháng 10 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue11 === undefined ? '' : this.params.actualEffortList[index].monthValue11); //  Tháng 11 (MD)
        totalMD +=
          ';' + (this.params.actualEffortList[index].monthValue12 === undefined ? '' : this.params.actualEffortList[index].monthValue12); //  Tháng 12 (MD)
        objectInsert2.monthValues = totalMD; //  Tổng MD sử dụng thực tế
        objectInsert2.numContract = this.params.actualEffortList[index].numberContract; //  số HĐ
        objectInsert2.numTTrPakd = this.params.actualEffortList[index].numberTTrPAKD; //  TTrPard
        //  call api insert thông tin nỗ lực sử dụng thực tế
        this.insertDataTTNLSDTT(objectInsert2, index);
      }
      this.onCancel(1);
    }
  }

  // insert data tới thông tin nỗ lực thực tế
  insertDataTTNLTT(outsourcingContractId: any, type: string) {
    const paramsActualEffortListLength = this.params.actualEffortList.length;
    if (paramsActualEffortListLength !== 0) {
      for (let index = 0; index < paramsActualEffortListLength; index++) {
        const objectInsert2: any = {};
        objectInsert2.id = this.params.actualEffortList[index].id; // id
        objectInsert2.createDate = ''; //  CreateDate
        objectInsert2.createUser = ''; //  UserCreateId
        objectInsert2.updateDate = ''; //  updateDate
        objectInsert2.UserModifyId = ''; //  UserModifyId
        if (type === 'add') {
          objectInsert2.isActive = 1;
        }
        objectInsert2.outsourcingContractId = outsourcingContractId; //  ID Hợp đồng thuê ngoài
        objectInsert2.projectId = this.params.actualEffortList[index].projectId; //  id dự án
        objectInsert2.softwareDevelopmentProjectId =
          this.params.actualEffortList[index].outsourcePlanId === '' ? 0 : this.params.actualEffortList[index].outsourcePlanId; //  kế hoạch thuê ngoài
        // objectInsert2.outsourcingOrganizationId = this.params.actualEffortList[index].outsourcingOrganizationId; //  id đơn vị thuê ngoài
        objectInsert2.outsourcingOrganizationName = this.params.actualEffortList[index].outsourcingOrganizationName; // tên đơn vị thuê ngoài
        objectInsert2.proposedCooperationClueUserId = this.params.actualEffortList[index].proposedCooperationClueUserId; //  đầu mối đề nghị hợp tác
        objectInsert2.estimateEffortStatus = this.params.actualEffortList[index].estimateEffortStatus; //  ULNL

        objectInsert2.businessOrganizationId = this.params.actualEffortList[index].businessOrganizationId; // Đơn vị kinh doanh
        objectInsert2.businessClueUserId = this.params.actualEffortList[index].businessClueUserId; //  Đầu mối kinh doanh
        objectInsert2.manufacturingOrganizationId = this.params.actualEffortList[index].manufacturingOrganizationId; //  Đơn vị sản xuất
        objectInsert2.technicalClueUserId = this.params.actualEffortList[index].technicalClueUserId; //  Đầu mối kỹ thuật

        objectInsert2.cooperationYear = this.params.actualEffortList[index].cooperationYear; //  Năm hợp tác
        objectInsert2.totalAppraisedMM = this.params.actualEffortList[index].totalAppraisedMM; //  Tổng MM đã thẩm định
        objectInsert2.totalUsedMM = this.params.actualEffortList[index].totalRealityMM; //  Tổng MM sử dụng thực tế
        let totalMD = '';
        totalMD += this.params.actualEffortList[index].monthValue1; //  Tháng 1 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue2; //  Tháng 2 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue3; //  Tháng 3 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue4; //  Tháng 4 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue5; //  Tháng 5 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue6; //  Tháng 6 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue7; //  Tháng 7 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue8; //  Tháng 8 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue9; //  Tháng 9 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue10; //  Tháng 10 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue11; //  Tháng 11 (MD)
        totalMD += ';' + this.params.actualEffortList[index].monthValue12; //  Tháng 12 (MD)
        objectInsert2.monthValues = totalMD; //  Tổng MD sử dụng thực tế
        objectInsert2.numContract = this.params.actualEffortList[index].numberContract; //  số HĐ
        objectInsert2.numTTrPakd = this.params.actualEffortList[index].numberTTrPAKD; //  TTrPard
        //  call api insert thông tin nỗ lực sử dụng thực tế
        this.insertDataTTNLSDTT(objectInsert2, index);
      }
    }
    this.onCancel(1);
  }

  //  get partnerCode by partnerId
  getPartnerCodeById(id: any) {
    this.contractManagerService.getPartnerCodeById(id).subscribe(res => (this.objPartner = res['data']));
  }

  //  validate data from undefine to ''
  validateDataUndefine(data: any) {
    if (data === 'undefined' || data === undefined) {
      return '';
    } else {
      return data;
    }
  }

  //  insert thông tin nỗ lực sử dụng thực tế
  insertDataTTNLSDTT(objectInsert2: any, index: number) {
    this.contractManagerService.doInsertDataTNLTT(objectInsert2).subscribe(res => {
      if (res) {
        // if (index >= 0) {
        //   this.toastService.openSuccessToast('thêm bản ghi thứ ' + (index + 1) + 'thông tin nỗ lực thực  thành công !');
        // }
      } else {
        this.toastService.openErrorToast(
          this.translateService.instant('thêm bản ghi thứ ' + (index + 1) + 'thông tin nỗ lực thực  thất bại !')
        );
      }
    });
  }

  //  trangnc
  onCancel(number?: any) {
    if (number === 1) {
      this.router.navigate(['/contract-management']);
    } else {
      const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
      modalRef.componentInstance.type = 'confirm';
      modalRef.componentInstance.param = this.translateService.instant('contractManagement.contractManagement');
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.router.navigate(['/contract-management']);
        }
      });
    }
  }

  //  trangnc
  onSearchUnit(event, params) {
    if (this.form.value['type'] === 'update') {
      //  search các autocomplete khi update
      const words = params.split(';');
      const type = words[0];

      if (type === 'duan') {
        this.listUnit$ = of([]);
        this.unitSearch1 = event.term;
        //  search cho mã dự án khi update
        const index = words[1];
        this.contractManagerService.getAllProjectCode(event.term).subscribe(res => {
          if (res['data'].length > 0) {
            this.params.actualEffortList[index].listUnit$ = of(res['data']);
          } else {
            this.params.actualEffortList[index].listUnit$ = of([]);
          }
        });
      } else if (type === 'doitac') {
        this.unitSearch = event.term;
        //  search cho mã đối tác khi update
        this.contractManagerService.getAllPartnerCode(event.term).subscribe(res => {
          if (res['data'].length > 0) {
            this.listDT$ = of(res['data']);
          } else {
            this.listDT$ = of([]);
          }
        });
      } else if (type === 'dmdnht') {
        this.unitSearch2 = event.term;
        //  search cho đầu mối đề nghị hợp tác khi update
        const index = words[1];
        this.contractManagerService.doSearchEmailOrName(event.term).subscribe(res => {
          if (res['data'].length > 0) {
            this.params.actualEffortList[index].listDMDVHT$ = of(res['data']);
          } else {
            this.params.actualEffortList[index].listDMDVHT$ = of([]);
          }
        });
      } else if (type === 'dmkd') {
        this.unitSearchDMKD = event.term;
        //  search cho đầu mối đề nghị hợp tác khi update
        const index = words[1];
        this.contractManagerService.doSearchEmailOrName(event.term).subscribe(res => {
          if (res['data'].length > 0) {
            this.params.actualEffortList[index].listDMKD$ = of(res['data']);
          } else {
            this.params.actualEffortList[index].listDMKD$ = of([]);
          }
        });
      } else if (type === 'dmkt') {
        this.unitSearchDMKT = event.term;
        //  search cho đầu mối đề nghị hợp tác khi update
        const index = words[1];
        this.contractManagerService.doSearchEmailOrName(event.term).subscribe(res => {
          if (res['data'].length > 0) {
            this.params.actualEffortList[index].listDMKT$ = of(res['data']);
          } else {
            this.params.actualEffortList[index].listDMKT$ = of([]);
          }
        });
      }
    } else if (this.form.value['type'] === 'add' || this.form.value['type'] === 'create') {
      this.unitSearch = event.term;
      const words = params.split(';');
      const index = words[1];
      const term = event.term;
      const objParam = term + ';' + params;
      if (term !== '') {
        this.debouncer.next(objParam);
      } else {
        this.params.actualEffortList[index].listUnit$ = of([]);
        //this.listUnit$ = of([]);
        this.listDT$ = of([]);
        this.listDMDVHT$ = of([]);
        this.listDMKT$ = of([]);
        this.listDMKD$ = of([]);
      }
    }
  }

  // trangnc
  debounceOnSearch() {
    this.debouncer.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => {
      this.loadDataOnSearchUnit(value);
    });
  }

  // trangnc - thêm mới call data cho autocomplete
  loadDataOnSearchUnit(term) {
    const words = term.split(';');
    const wordSearch = words[0];
    const typeSearch = words[1];

    if (typeSearch === 'duan') {
      //  search cho mã dự án
      const index = words[2];
      this.unitSearch1 = wordSearch;
      this.contractManagerService.getAllProjectCode(wordSearch).subscribe(res => {
        if (res['data'].length > 0) {
          this.params.actualEffortList[index].listUnit$ = of(res['data']);
        } else {
          this.params.actualEffortList[index].listUnit$ = of([]);
        }
      });
    } else if (typeSearch === 'dmdnht') {
      //  search cho đầu mối đơn vị hợp tác
      const index = words[2];
      this.unitSearch2 = wordSearch;
      this.contractManagerService.doSearchEmailOrName(wordSearch).subscribe(res => {
        if (res['data'].length > 0) {
          this.params.actualEffortList[index].listDMDVHT$ = of(res['data']);
        } else {
          this.params.actualEffortList[index].listDMDVHT$ = of([]);
        }
      });
    } else if (typeSearch === 'doitac') {
      //  search cho mã đối tác
      this.contractManagerService.getAllPartnerCode(wordSearch).subscribe(res => {
        if (res['data'].length > 0) {
          this.listDT$ = of(res['data']);
        } else {
          this.listDT$ = of([]);
        }
      });
    } else if (typeSearch === 'dmkd') {
      //  search cho đầu mối kinh doanh
      const index = words[2];
      this.unitSearchDMKD = wordSearch;
      this.contractManagerService.doSearchEmailOrName(wordSearch).subscribe(res => {
        if (res['data'].length > 0) {
          this.params.actualEffortList[index].listDMKD$ = of(res['data']);
        } else {
          this.params.actualEffortList[index].listDMKD$ = of([]);
        }
      });
    } else if (typeSearch === 'dmkt') {
      //  search cho  đầu mối kĩ thuật
      const index = words[2];
      this.unitSearchDMKT = wordSearch;
      this.contractManagerService.doSearchEmailOrName(wordSearch).subscribe(res => {
        if (res['data'].length > 0) {
          this.params.actualEffortList[index].listDMKT$ = of(res['data']);
        } else {
          this.params.actualEffortList[index].listDMKT$ = of([]);
        }
      });
    }
  }

  //  trangnc get all plan theo planstatus = 'phê duyệt'
  getAllPlan() {
    this.contractManagerService.getAllPlanFollowApprove().subscribe(res => (this.outsourcePlans = res['data']));
  }


  getAllRole() {
    this.listUnit$ = of([]);
    const data = {
      keySearch: '',
      type: 'DEPART'
    }
    this.organizationCategoriesService.getStatusOverviewList(data).subscribe(
      result => {
        if (result) {
          const dataRes: any = result;
          this.listRole = dataRes;
        } else {
          this.listRole = [];
        }
      },
      err => {
        this.listRole = [];
      }
    );
  }

  //  trangnc get all năm hợp tác
  getAllYear() {
    this.contractManagerService.getSignStatusOrContractStatus('NHT').subscribe(res => (this.cooperationYearList = res));
  }

  // trangnc
  //  convert date format Mon May 18 2020 12:00:00 GMT+0700 (Indochina Time) ---> yyyy/MM/dd
  convertDate(str) {
    const date = new Date(str),
      mnth = ('0' + (date.getMonth() + 1)).slice(-2),
      day = ('0' + date.getDate()).slice(-2);
    return [date.getFullYear(), mnth, day].join('-');
  }

  // trangnc chọn select trong khtn -> hiển thị column đơn vị kinh doanh, đầu mối kinh doanh, đơn vị sản xuất, đầu mối kĩ thuật
  callAll(index, value) {
    if (value !== '') {
      this.contractManagerService.getBusiness(value).subscribe(res => {
        this.params.actualEffortList[index].businessOrganizationId = res.data[0].businessOrg; // id đơn vị kinh doanh
        this.params.actualEffortList[index].businessClueUserId = res.data[0].am; // id đầu mối kinh doanh
        this.params.actualEffortList[index].manufacturingOrganizationId = res.data[0].productionOrg; //id  đơn vị sản xuất
        this.params.actualEffortList[index].technicalClueUserId = res.data[0].pm; // id đầu mối kỹ thuật
        //
        this.params.actualEffortList[index].flagOutSourcingPlan = true; // ẩn đi không cho phép nhập

        this.params.actualEffortList[index].businessOrgName = res.data[0].businessOrgName; // tên đơn vị kinh doanh
        this.params.actualEffortList[index].amName = res.data[0].amName; // đầu mối kinh doanh
        this.params.actualEffortList[index].productionOrgName = res.data[0].productionOrgName; // đơn vị sản xuất
        this.params.actualEffortList[index].pmName = res.data[0].pmName; // đầu mối kỹ thuật
        // mmOS -> Tổng MM đã thẩm định
        const dataMMOS = res.data[0].mmOs;
        if (dataMMOS !== '' && dataMMOS !== null) {
          this.params.actualEffortList[index].totalAppraisedMM = res.data[0].mmOs;
          this.params.actualEffortList[index].flagMMOS = true; // ẩn đi không cho phép nhập
        } else {
          this.params.actualEffortList[index].totalAppraisedMM = '';
          this.params.actualEffortList[index].flagMMOS = false; //  cho phép nhập
        }
        // add tới list đầu mối kinh doanh - đầu mối kĩ thuật
        const dataDMKD = {
          id: res.data[0].am,
          name: res.data[0].amName
        };

        const dataDMKT = {
          id: res.data[0].pm,
          name: res.data[0].pmName
        };

        this.params.actualEffortList[index].listDMKD$ = of([dataDMKD]);
        this.params.actualEffortList[index].listDMKT$ = of([dataDMKT]);
        this.onChangeTotalMD(index);
      });
    } else {
      this.params.actualEffortList[index].flagOutSourcingPlan = false; // hiển thị cho phép nhâp
      this.params.actualEffortList[index].flagMMOS = false; //  cho phép nhập

      this.params.actualEffortList[index].businessOrganizationId = ''; // id đơn vị kinh doanh
      this.params.actualEffortList[index].businessClueUserId = ''; // id đầu mối kinh doanh
      this.params.actualEffortList[index].manufacturingOrganizationId = ''; //id  đơn vị sản xuất
      this.params.actualEffortList[index].technicalClueUserId = ''; // id đầu mối kỹ thuật

      this.params.actualEffortList[index].businessOrgName = ''; // tên đơn vị kinh doanh
      this.params.actualEffortList[index].amName = ''; // tên đầu mối kinh doanh
      this.params.actualEffortList[index].productionOrgName = ''; // tên đơn vị sản xuất
      this.params.actualEffortList[index].pmName = ''; // tên đầu mối kỹ thuật

      this.params.actualEffortList[index].totalAppraisedMM = ''; // MMOS

    }
    this.onChangeTotalMD(index);
  }

  // trangnc clear function autocomplete
  onClearUnit(type: any, index?: number) {
    if (type === 'doitac') {
      this.listDT$ = of([]);
      this.unitSearch = '';
    } else if (type === 'duan') {
      this.params.actualEffortList[index].listUnit$ = of([]);
      this.unitSearch1 = '';
    }
  }
  // trangnc onSearchUnitClose() cho autocomplete
  onSearchUnitClose(type: any, index?: number) {
    if (type === 'doitac') {
      this.listDT$ = of([]);
      this.unitSearch = '';
    } else if (type === 'duan') {
      this.params.actualEffortList[index].listUnit$ = of([]);
      this.unitSearch1 = '';
    }
  }

  // validate mã đối tác khi trống
  validateEmpty() {
    const val = this.form.value.partnerId;
    const size = val === null ? 0 : val.length;
    if (size <= 0) {
      this.clErrorPartner = 'has-error';
      this.errorPartner = 'paterCode';
      this.messageErrorPartner = 'Mã đối tác là bắt buộc';
    } else {
      this.clErrorPartner = '';
      this.errorPartner = '';
      this.messageErrorPartner = '';
    }
  }


  //
  customSearchFn(term: string, item: any) {
    const replacedKey = term.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    const newRegEx = new RegExp(replacedKey, 'gi');
    const purgedPosition = item.name.replace(REGEX_PATTERN.SEARCH_DROPDOWN_LIST, '');
    return newRegEx.test(purgedPosition);
  }

  onChangePosition(event, index, flag) {
    if (event) {
      if (flag === 'dvsx') {
        this.params.actualEffortList[index].manufacturingOrganizationId = event.id;
      } else if (flag === 'dvkd') {
        this.params.actualEffortList[index].businessOrganizationId = event.id;
      }
    }
  }

  onClearPosition(index, flag) {
    if (flag === 'dvsx') {
      this.params.actualEffortList[index].manufacturingOrganizationId = '';
    } else if (flag === 'dvkd') {
      this.params.actualEffortList[index].businessOrganizationId = '';
    }
  }
  onChangePosition1(event) {
    if (event) {
      this.setValueToField('id', event.id);
      this.setValueToField('code', event.code);
    }
  }
  setValueToField(item, data) {
    this.form.get(item).setValue(data);
  }

  onClearPosition1() {
    this.setValueToField('code', null);
    this.setValueToField('code', null);
  }
  onPaste(event: ClipboardEvent, nameField?: string) {
    const clipboardData = event.clipboardData;
    const pastedText = clipboardData.getData('text');
    if (pastedText.length <= 1000) {
      this.form.get(nameField).setValue(pastedText);
      return false;
    } else if (pastedText.length > 1000) {
      const res = pastedText.substring(0, 1000);
      this.form.get(nameField).setValue(res);
      return false;
    }
  }

  onPasteL(event: ClipboardEvent, nameField?: string, index?: number, size?: number) {
    const clipboardData = event.clipboardData;
    const pastedText = clipboardData.getData('text');
    let numberContract = '';
    if (pastedText.length < size) {
      numberContract = pastedText;
    } else if (pastedText.length >= size) {
      const res = pastedText.substring(0, size - 1);
      numberContract = res;
    }
    if (nameField === 'numberContract') {
      this.params.actualEffortList[index].numberContract = numberContract;
      return false;
    } else if (nameField === 'outsourcingOrganizationName') {
      this.params.actualEffortList[index].outsourcingOrganizationName = numberContract;
      return false;
    } else if (nameField === 'numberTTrPAKD') {
      this.params.actualEffortList[index].numberTTrPAKD = numberContract;
      return false;
    }
  }

  // page
  // previousPage() {
  //   if (this.currentPage !== 1) {
  //     this.currentPage -= 1;
  //     this.loadList();
  //   }
  // }

  nextPage() {
    if (this.currentPage !== this.numberOfPages) {
      this.currentPage += 1;
      this.loadList();
    }
  }

  loadList() {
    const begin = (this.currentPage - 1) * this.numberPerPage;
    const end = begin + Number(this.numberPerPage);
    // this.params.actualEffortList = this.pageList.slice(begin, end);
    this.params.actualEffortList2 = this.pageList.slice(begin, end);
  }

  arrPage() {
    this.pageList = this.params.actualEffortList;
    this.numberOfPages = Math.ceil(this.pageList.length / this.numberPerPage);
    this.arrNumberOfPages = Array(this.numberOfPages).fill(4);
    // cập nhật lại số bản ghi
    this.numRecord = this.params.actualEffortList.length;
  }

  firstPage() {
    this.currentPage = 1;
    this.loadList();
  }

  lastPage() {
    this.currentPage = this.numberOfPages;
    this.loadList();
  }

  checkFirstAndPrev() {
    if (this.currentPage === 1) {
      return 'disabled';
    } else {
      return '';
    }
  }

  checkNextAndLast() {
    if (this.currentPage === this.numberOfPages) {
      return 'disabled';
    } else {
      return '';
    }
  }

  checkActive(k: number) {
    if (this.currentPage === k + 1) {
      return 'active';
    } else {
      return '';
    }
  }

  changePage(num: number) {
    this.currentPage = num + 1;
    this.loadList();
  }
  onSearchUnit2(event, index) {
    // eslint-disable-next-line no-debugger
    //debugger;
    this.unitSearch4 = event.term;
    const term = event.term + ';' + index;
    if (term !== '') {
      this.debouncer2.next(term);
    } else {
      this.lstHuman[index].listUnit2$ = of([]);
    }
  }
  debounceOnSearch2() {
    this.debouncer2.pipe(debounceTime(TIME_OUT.DUE_TIME_SEARCH)).subscribe(value => this.loadDataOnSearchUnit21(value));
  }
  loadDataOnSearchUnit21(term) {
    // eslint-disable-next-line no-debugger
    // debugger;
    const vale = term.split(';');
    const index = vale[1];

    const data = {
      keySearch: vale[0],
      type: ''
    }


    this.organizationCategoriesService.searchHumanResource(data).subscribe(res => {
      if (this.unitSearch4) {
        const dataRes: any = res;
        for (let i = 0; i < dataRes.length; i++) {
          if (dataRes[i].username == null) {
            dataRes[i].username = '';
          }
        }
        this.lstHuman[index].listUnit2$ = of(dataRes.sort((a, b) => a.username.localeCompare(b.username)));
        //  this.listUnit2$ = of(dataRes.sort((a, b) => a.email.localeCompare(b.email)));

      } else {
        this.listUnit2$ = of([]);
      }
    });
  }
  onClearUnit2() {
    this.listUnit2$ = of([]);
    this.unitSearch4 = '';
  }

  onSearchUnitClose2() {
    if (!this.form.value.username) {
      this.listUnit2$ = of([]);
      this.unitSearch4 = '';
    }
  }


  displayFieldHasError(field: string) {
    return {
      'has-error': this.isFieldValid(field)
    };
  }

  onsaveHuman() {
    const data = this.selectedData;
    data.lstProjectMember = this.lstHuman;
    data.lstProjectMemberDelete = this.lstDelete;
    // const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
    // modalRef.componentInstance.type = 'delete';
    // modalRef.componentInstance.param = 'muốn thêm mới dự án';
    // modalRef.componentInstance.onCloseModal.subscribe(value => {
    // if (value === true) {
    this.buttonDisabled = true;
    this.organizationCategoriesService.saveHumanToProject(data).subscribe(res => {
      // this.activeModal.dismiss();
      this.form.reset();
      // this.activeModal.dismiss(true);
      this.toastService.openSuccessToast('Thêm mới dữ liệu thành công!');
      this.router.navigate(['system-categories/project-management']);
      this.buttonDisabled = false;
    },
      error => {
        this.toastService.openErrorToast('Thêm mới dữ liệu không thành công!');
        this.buttonDisabled = true;
      });
    //   }
    // });
  }



  onChangePage(value?: number) {
    this.numberPerPage = value;
    this.numberOfPages = Math.ceil(this.pageList.length / this.numberPerPage);
    //nếu số trang hiên tại lớn tổng số page -> page cuối cùng
    if (this.numberOfPages < this.currentPage) {
      this.currentPage = this.numberOfPages;
    }
    this.arrNumberOfPages = Array(this.numberOfPages).fill(4);
    // cập nhật lại số bản ghi
    this.numRecord = this.params.actualEffortList.length;
    this.loadList();
  }

  onNotUpdate(index) {
    if (this.objClone1[index] != null) {
      this.lstHuman[index] = this.objClone1[index];
      // this.status = true;
      this.isEdit[index] = !this.isEdit[index];
      //    this.isEdit.unshift(true);
    }
    else {
      this.lstHuman.splice(index, 1);
      this.isEdit[index] = true;
    }
    this.buttonDisabled = false;
  }

  //  trangnc
  onNotUpdateEffort(effortIndex) {
    const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
    modalRef.componentInstance.type = 'confirm';
    modalRef.componentInstance.param = this.translateService.instant('contractManagement.contractManagement');

    modalRef.componentInstance.onCloseModal.subscribe(value => {
      this.spinner.show();
      if (value === true) {
        this.doNotUpdateEffort(effortIndex);
      }
      this.spinner.hide();
    });
  }

  validate(index) {
    if (this.lstHuman[index].department === "") {
      this.toastService.openWarningToast('Vai trò nhân sự không được để trống');
      return false;
    }
    if (this.lstHuman[index].humanResourceId === "") {
      this.toastService.openWarningToast('Nhân sự không được để trống');
      return false;
    }
    if (this.lstHuman[index].dateJoin === "") {
      this.toastService.openWarningToast('Ngày bắt đầu không được để trống');
      return false;
    }
    if (this.lstHuman[index].dateOut === "") {
      this.toastService.openWarningToast('Ngày kết thúc không được để trống');
      return false;
    }
    if (this.lstHuman[index].resources === "") {
      this.toastService.openWarningToast('% Nguồn lực không được để trống');
      return false;
    }
    if (new Date(this.lstHuman[index].dateJoin) > new Date(this.lstHuman[index].dateOut)) {
      this.toastService.openWarningToast('Ngày bắt đầu đang lớn hơn ngày kết thúc');
      return false;
    }

    if (this.lstHuman[index].resources < 0) {
      this.toastService.openWarningToast('Nguồn lực phải lớn hơn 0');
      return false;
    }

    if (this.lstHuman[index].resourcesUsed < 0) {
      this.toastService.openWarningToast('Nguồn lực đã sử dụng phải lớn hơn 0');
      return false;
    }

    // if (this.lstHuman[index].resourcesUsed + this.lstHuman[index].resources > 150) {
    //   this.toastService.openWarningToast('Tổng nguồn lực và nguồn lực đã sử dụng không được lớn hơn 150');
    //   return false;
    // }
    return true;
  }

  onUpdateEffort(index) {
    if (this.validate(index)) {
      this.isEdit[index] = !this.isEdit[index];
      if (!this.checkIsEditing()) {
        this.buttonDisabled = false;
      }
    }
  }
  checkIsEditing() {
    for (let i = 0; i < this.isEdit.length; i++) {
      if (!this.isEdit[i]) {
        return true;
      }
    }
    return false;
  }
  //  trangnc
  onClickEditEffort(effortIndex) {
    this.isEdit[effortIndex] = !this.isEdit[effortIndex];
    this.objClone1[effortIndex] = JSON.parse(JSON.stringify(this.lstHuman[effortIndex]));
    this.buttonDisabled = true;
  }
  onDelete(i) {
    if (this.lstHuman[i].role === "PM") {
      this.toastService.openWarningToast('Không được phép xóa PM/TeamLead của dự án!');
    } else if (this.lstHuman[i].role === "BM") {
      this.toastService.openWarningToast('Không được phép xóa BA manager của dự án!');
    } else if (this.lstHuman[i].role === "QM") {
      this.toastService.openWarningToast('Không được phép xóa QA manager của dự án!');
    } else if (this.lstHuman[i].role === "TL") {
      this.toastService.openWarningToast('Không được phép xóa Test Leader của dự án!');
    } else {
      this.isModalConfirmShow = true;
      const modalRef = this.modalService.open(ConfirmModalComponent, { centered: true, backdrop: 'static' });
      modalRef.componentInstance.type = 'delete';
      modalRef.componentInstance.param = 'nhân sự này';
      // modalRef.componentInstance.message = 'Bạn có muốn xóa nhân sự này không?';
      modalRef.componentInstance.onCloseModal.subscribe(value => {
        if (value === true) {
          this.lstDelete.push(this.lstHuman[i]);
          this.lstHuman.splice(i, 1);
          this.isEdit.splice(i, 1);
        }
        this.isModalConfirmShow = false;
      });
    }
  }
  onClickAddEffort() {
    const obj: any = {
      department: '',
      humanResourceId: '',
      dateJoin: '',
      dateOut: '',
      resources: '',
      resourcesUsed: '',
      isActive: '',
    };
    // this.isEdit[0]=false;
    this.isEdit.unshift(false);
    this.lstHuman.unshift(obj);
    this.buttonDisabled = true;
    // this.ischeck=true;

  }

  onChange(item, index) {
    this.lstHuman[index].resourcesUsed = item.resourcesUsed;
  }

  loadAlProjectMember() {
    this.loadAll(1);
  }

  loadProjectMemberHistory() {
    console.log(this.lstHuman);
    this.ischeck = false;
    this.loadAll(0);
  }

  loadAll(isActive) {
    const data = this.selectedData;
    this.selectedData.isActive = isActive;
    this.organizationCategoriesService.getListProjectMember(data).subscribe(response => {
      if (response && response.data) {
        this.lstHuman = response.data;
        for (let i = 0; i <= this.lstHuman.length; i++) {
          this.isEdit[i] = true;
          if (isActive !== 1 && this.lstHuman[i].isActive === 0) {
            this.isEdit[i] = false;
          }
          const objj: any = {
            humanResourceId: this.lstHuman[i].humanResourceId,
            username: this.lstHuman[i].username
          }
          this.lstHuman[i].listUnit2$ = of([objj]);
        }
      }
    });
  }

  back() {
    this.ischeck = true;
    this.loadAlProjectMember();
  }
}
