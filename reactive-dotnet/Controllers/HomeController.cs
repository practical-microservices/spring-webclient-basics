using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Reactive.Linq;
using System.Reactive.Threading.Tasks;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Net.Http.Headers;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace ReactiveDotnet.Controllers
{
    public class HomeController : Controller
    {
        [Route("/observable")]
        public async Task<IActionResult> Observable()
        {
            var client = new HttpClient();
            return await client.GetStringAsync("https://reqres.in/api/users?page=2")
                .ToObservable()
                .SelectMany(x => JObject.Parse(x) // parse http reply into json
                    .SelectTokens("$.data[*].first_name") // run Json Query
                    .Select(y => (string)y)) // cast results to string
                .SelectMany(x => client
                    .GetStringAsync($"https://devops.datenkollektiv.de/renderBannerTxt?font=soft&text={x}") // run async web request for each name
                    .ToObservable()) 
                .Aggregate((prev,current) => prev + current + "\r\n") // concatenate all results into a single string
                .Select(Content) // return it as body content
                .ToTask(); // transition back into Task (Java Future equivalent)
        }

        [Route("/")]
        public async Task<IActionResult> Async()
        {
            var client = new HttpClient();
            // resume method execution after call is complete - async
            var response = await client.GetStringAsync("https://reqres.in/api/users?page=2");
            // use LINQ to extract names from json
            var names = JObject.Parse(response)
                .SelectTokens("$.data[*].first_name")
                .Select(x => (string) x)
                .ToList();

            // spin up tasks to retrieve web request banners
            var getBannerTasks = names
                .Select(name => client.GetStringAsync($"https://devops.datenkollektiv.de/renderBannerTxt?font=soft&text={name}"))
                .ToArray();
            // wait for all banners to finish executing - comes back as array list
            var banners = await Task.WhenAll(getBannerTasks);
            // concat list into with new line as delimiter
            var body = string.Join("\r\n", banners);
            return Content(body);
        }
       
    }
}