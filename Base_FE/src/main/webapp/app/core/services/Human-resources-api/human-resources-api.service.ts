import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {SERVER_API} from "app/shared/constants/api-resource.constants";


@Injectable({
  providedIn: 'root'
})
export class HumanResourcesApiService {
  private baseUri = SERVER_API;
  private token = localStorage.getItem('token');
  constructor(private http: HttpClient) {
  }

  // TanNV
  searchHumanResources(searchForm?: any): Observable<any> {
    return this.http.post<any>(this.baseUri + '/humanResources/searchHumanResources', searchForm);
  }

  deleteHumanResources(id?: any): Observable<any> {
    return this.http.delete<any>(SERVER_API + '/humanResources/deleteHumanResources/' + id);
  }

  getDepartment(obj: any): Observable<any> {
    return this.http.post<any>(this.baseUri + '/app-param/getAppParam', obj);
  }

  resetpassword(id) {
    const option = {
      headers: new HttpHeaders({
        'Authorization':'Bearer '+ this.token
      })
    }
    return this.http.put<any>(SERVER_API + '/humanResources/reset-password/' + id, id, option);
  }
  resetPasswordByEmail(email?: string, key?: string): Observable<HttpResponse<any>> {
    return this.http.get<HttpResponse<any>>(SERVER_API + '/authen/verify-email-forgot-password?email=' + email + '&&key=' + key);
  }

  forgotPassword(email?: string): Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(SERVER_API + '/authen/forgot-password', {email: email});
  }

  getlistHumanResourcesDepatment(name: any): Observable<any> {
    return this.http.get(this.baseUri + '/group-permission/getlistHumanResourcesDepatment?name=', name);
  }

  // getDepartment(obj: any): Observable<any> {
  //   return this.http.post<any>(this.baseUri + '/app-param/getAppParam', obj);
  // }

  //End TanNV

  /*duc service*/

  getDepartmentList(): Observable<any> {
    return this.http.get(SERVER_API + '/humanResources/getGroupUser');
  }

  getCenterList(): Observable<any> {
    return this.http.get(SERVER_API + '/humanResources/getHumanCenter');
  }

  save(data): Observable<any> {
    return this.http.post<any>(SERVER_API + '/humanResources/add', data);
  }

  update(data): Observable<HttpResponse<any>> {
    return this.http.post<any>(SERVER_API + '/humanResources/update', data, {observe: 'response'});

  }

  getInfo(Id): Observable<any> {
    return this.http.get(SERVER_API + '/humanResources/get-human-by-id/' + Id);
  }

  checkEmail(email): Observable<any> {
    return this.http.get(SERVER_API + '/humanResources/check-email/' + email);
  }

  checkUsername(username): Observable<any> {
    return this.http.get(SERVER_API + '/humanResources/check-username/' + username);
  }

  /*end duc*/

  //nuctv
  synchronizedUser(humanResourceID:any):Observable<any>{
    return this.http.get(SERVER_API+'/humanResources/synchronized/'+humanResourceID);
  }

  //end nuctv
}
