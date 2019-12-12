package baikal.web.footballapp;

import baikal.web.footballapp.model.ActiveMatch;
import baikal.web.footballapp.model.ActiveMatches;
import baikal.web.footballapp.model.Advertisings;
import baikal.web.footballapp.model.Announce;
import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.DataClub;
import baikal.web.footballapp.model.EditProfile;
import baikal.web.footballapp.model.Invite;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Match;
import baikal.web.footballapp.model.MatchPopulate;
import baikal.web.footballapp.model.Matches;
import baikal.web.footballapp.model.News_;
import baikal.web.footballapp.model.ParticipationRequest;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonPopulate;
import baikal.web.footballapp.model.PersonStats;
import baikal.web.footballapp.model.RefereeRequestList;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.ServerResponse;
import baikal.web.footballapp.model.SetRefereeList;
import baikal.web.footballapp.model.SignIn;
import baikal.web.footballapp.model.Stadium;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.TeamStats;
import baikal.web.footballapp.model.Tourney;
import baikal.web.footballapp.model.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FootballApi {
    //get all news
    @GET("/api/crud/news")
    Call<List<News_>> getAllNewsCrud(@Query("_limit") String limit, @Query("_offset") String offset);

    //get all news
    @GET("/api/crud/announce")
    Observable<List<Announce>> getAllAnnounce(@Query("_limit") String limit, @Query("_offset") String offset);

    //get advertising
    @GET("/api/ads")
    Observable<Advertisings> getAdvertising(@Query("limit") String limit, @Query("offset") String offset);
    @GET("/api/crud/league?status=started&_select=_id")
    Call<List<League>> getStartedLeagues();
    @GET("/api/crud/match?played=false&_sort=date&_populate=teamOne+teamTwo")
    Call<List<ActiveMatch>> getUpcomingMatches(@Query("date") String date, @Query("league") String league, @Query("_limit") String limit);

    //get all clubs
    @GET("/api/clubs")
    Call<List<Club>> getAllClubs();

    @GET("/api/stats/person?_populate=person")
    Call<List<PersonStats>> getPersonStats(@Query("person") String person, @Query("on_") String on);




    //get coming matches
    @GET("/api/matches/upcoming")
    Observable<ActiveMatches> getComingMatches();

    //get referees type==referee
    //get players type==player
    @GET("/api/crud/person")
    Observable<List<Person>> getAllPersons(@Query("surname") String surname,@Query("_limit") String limit, @Query("_offset") String offset);
    @GET("/api/crud/person")
    Observable<List<Person>> getAllPerson(@Query("_limit") String limit, @Query("_offset") String offset);
    @GET("/api/crud/person?_sort=-createdAt")
    Call<List<Person> >getAllPersonsWithSort(@Query("_limit") String limit, @Query("createdAt") String createdAt);
    @GET("/api/crud/person/or?_sort=-createdAt")
    Call<List<Person> >getFilteredPersonsWithSort(@Query("surname") String surname, @Query("name") String name, @Query("lastname") String lastname, @Query("_limit") String limit, @Query("_offset") String offset);
    @GET("/api/crud/tourney")
    Observable<List<Tourney>> getTourneys(@Query("name") String name, @Query("region") String region);
    @GET("/api/crud/tourney")
    Observable<List<Tourney>> getTourneysById(@Query("_id") String id);
    @GET("/api/crud/league?_populate=matches+stages")
    Call<List<League>> getAllLeagues(@Query("_limit") String limit, @Query("_offset") String offset);
    @GET("/api/crud/league?_populate=matches+stages")
    Call<List<League>> getLeaguesByTourney(@Query("tourney") String tourney);
    @GET("/api/crud/match?_sort=date")
    Call<List<Match>> getMatchesForNews(@Query("date") String date, @Query("league") String league, @Query("_limit") String limit);
    @GET("api/crud/match?_populate=teamOne+teamTwo+place")
    Call<List<MatchPopulate>> getMatches(@Query("_id") String id, @Query("referees.person") String referee);
    @GET("api/crud/match?_populate=teamOne+teamTwo+place")
    Call<List<MatchPopulate>> getMatchById(@Query("_id") String id);
    //edit web
    @PATCH("/api/crud/match/{id}")
    Call<Match> editMatch(@Path("id") String id, @Header("auth") String authorization, @Body Match match);
    @POST("/api/matches/changeProtocol/{id}")
    Observable<Match> editProtocolMatch(@Path("id") String id, @Header("auth") String authorization, @Body Match match);

    @Multipart
    @PATCH("/api/crud/person/{id}")
    Call<Person> editProfile(@Path("id") String id, @Header("auth") String authorization, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);
    @Multipart
    @PATCH("/api/crud/person/{id}")
    Call<EditProfile> editPlayerInfo(@Path("id") String id, @Header("auth") String authorization, @Part("favoriteTourney") List<RequestBody> favoriteTourney);

    @GET("/api/crud/person?_populate=favoriteTourney&_select=favoriteTourney")
    Call<List<PersonPopulate> >getFavTourneysByPerson(@Query("_id") String id);
    @GET("/api/crud/person?_select=favoriteTourney")
    Call<List<Person> >getFavTourneysId(@Query("_id") String id);
    @GET("/api/crud/stadium")
    Call <List<Stadium>> getStadium (@Query("tourney") String tourney, @Query("_id") String id);
    @GET("api/crud/person")
    Call<List<Person>> getPerson(@Query("_id") String id);

    //edit match protocol referees
    @POST("/api/matches/setreferees")
    Observable<Response<Matches>> editProtocolReferees(@Header("auth") String authorization, @Body RefereeRequestList body);

    //set referee
    @POST("/api/matches/setmultireferees")
    Observable<Response<ServerResponse>> editMatch(@Header("auth") String authorization, @Body SetRefereeList body);

    @GET("/api/person_invite?_populate=team")
    Call<List<Invite>> getInvites(@Query("person") String person, @Query("team") String team, @Query("status") String status);
    @GET("/api/person_invite?_populate=team&status=accepted")
    Call<List<Invite>> getUsersTeams(@Query("person") String person);
    //refresh web
    @GET("/api/refresh")
    Observable<User> refreshUser(@Header("auth") String authorization);

    //sign up web
    @Multipart
    @POST("/api/signup")
    Call<User> signUp(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);


    //sign in web
    @POST("/api/signin")
    Call<User> signIn(@Body SignIn body);

    //add player to team
    @Multipart
    @POST("/api/person_invite")
    Call<ServerResponse> addPlayerToTeam(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params);

    //create new team
    @Multipart
    @POST("/api/crud/team")
    Call<Team> addTeam(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("/api/participation_request")
    Call<Team> addTeamToLeague(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params);

    //edit team
    @PATCH("/api/crud/team/{id}")
    Call<Team> editTeam(@Path("id") String id, @Header("auth") String authorization, @Body Team body);

    //player inv
    @POST("/api/person_invite/accept/{id}")
    Call<User> playerInv(@Path("id") String id, @Header("auth") String authorization);
    @POST("/api/person_invite/reject/{id}")
    Call<Invite> rejectInv(@Path("id") String id, @Header("auth") String authorization);

    @POST("/api/person_invite/cancel/{id}")
    Call<Invite> cancelInv(@Path("id") String id, @Header("auth") String authorization);
    @GET("api/crud/team?_populate=players")
    Call<List<Team>> getTeams(@Query("creator") String creator);
    @GET("/api/participation_request")
    Call<List<ParticipationRequest>> getParticipation(@Query("team") String id);
    @GET("api/crud/league?_populate=matches")
    Call<List<League>> getMainRefsLeagues(@Query("mainReferee") String mainRefId);

    @GET("/api/crud/team?_populate=players")
    Call<List<Team>> getTeam(@Query("_id") String id);

    @GET("/api/stats/team")
    Observable<List<TeamStats>> getTeamStats(@Query("_id") String id);

    //add new club
    @Multipart
    @POST("/api/clubs/add")
    Call<DataClub> addClub(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    //edit club
    @Multipart
    @POST("/api/clubs/edit")
    Call<DataClub> editClub(@Header("auth") String authorization, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    @GET("/api/crud/region")
    Call<List<Region>> getRegions();
    @GET("/api/crud/tourney")
    Call<List<Tourney>> getAllTourneys();

    @POST("api/matches/changePlayers/{id}")
    Call<Match> changePlayersForMatch(@Path("id") String id, @Header("auth") String token, @Body List<String> players);
}

