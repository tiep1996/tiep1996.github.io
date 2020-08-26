import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JhiMainComponent } from 'app/layouts/main/main.component';

const routes: Routes = [
  {
    path: '',
    component: JhiMainComponent,
    children: [
      {
        path: '',
        // data: {
        //   authorities: ['ROLE_USER']
        // },
        /* canActivate: [UserRouteAccessService],*/
        loadChildren: () => import('../../home/home.module').then(m => m.InvoiceWebappHomeModule)
      },
      {
        path: 'system-categories',
        /*canActivate: [UserRouteAccessService],*/
        loadChildren: () => import('../../modules/system-categories/system-categories.module').then(m => m.SystemCategoriesModule)
      },

      // {
      //   path: 'temp',
      //   /*tempcanActivate: [UserRouteAccessService],*/
      //   loadChildren: () => import('../../modules/temp/test.module').then(m => m.TestModule)
      // },

    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class InvoiceWebapMainRoutingModule {}
