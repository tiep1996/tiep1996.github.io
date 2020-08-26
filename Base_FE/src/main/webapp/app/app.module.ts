import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import './vendor';
import {InvoiceWebappSharedModule} from 'app/shared/shared.module';
import {InvoiceWebappCoreModule} from 'app/core/core.module';
import {InvoiceWebappAppRoutingModule} from './app-routing.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {JhiMainComponent} from './layouts/main/main.component';
import {NavbarComponent} from './layouts/navbar/navbar.component';
import {FooterComponent} from './layouts/footer/footer.component';
import {PageRibbonComponent} from './layouts/profiles/page-ribbon.component';
import {ActiveMenuDirective} from './layouts/navbar/active-menu.directive';
import {ErrorComponent} from './layouts/error/error.component';
import {SidebarComponent} from 'app/layouts/sidebar/sidebar.component';
import {AppComponent} from 'app/app.component';
import {InvoiceWebappMainModule} from 'app/layouts/main/main.module';
import {ChartsModule} from 'ng2-charts';
import {LoginComponent} from './account/login/login.component';
import {ChangePasswordComponent} from "app/layouts/navbar/change-password/change-password.component";
import {ForgotPasswordComponent} from './account/login/forgot-password/forgot-password.component';
import { VerifyForgotPasswordComponent } from './account/login/forgot-password/verify-forgot-password/verify-forgot-password.component';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    InvoiceWebappSharedModule,
    InvoiceWebappCoreModule,
    // InvoiceWebappHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    // InvoiceWebappEntityModule,
    InvoiceWebappMainModule,
    InvoiceWebappAppRoutingModule,
    ChartsModule,

  ],
  declarations: [
    AppComponent,
    JhiMainComponent,
    NavbarComponent,
    SidebarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    LoginComponent,
    ChangePasswordComponent,
    ForgotPasswordComponent,
    VerifyForgotPasswordComponent,
  ],
  exports: [], entryComponents: [ChangePasswordComponent, ForgotPasswordComponent],
  bootstrap: [AppComponent]
})
export class InvoiceWebappAppModule {
}
