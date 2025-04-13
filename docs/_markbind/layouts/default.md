<head-bottom>
  <link rel="stylesheet" href="{{baseUrl}}/stylesheets/main.css">
</head-bottom>

<header sticky>
  <navbar type="dark">
    <a slot="brand" href="{{baseUrl}}/index.html" title="Home" class="navbar-brand">Sharkives</a>
    <li><a href="{{baseUrl}}/index.html" class="nav-link">Home</a></li>
    <li><a href="{{baseUrl}}/features.html" class="nav-link">Features</a></li>
    <li><a href="{{baseUrl}}/UserGuide.html" class="nav-link">User Guide</a></li>
    <li><a href="{{baseUrl}}/DeveloperGuide.html" class="nav-link">Developer Guide</a></li>
    <li><a href="{{baseUrl}}/AboutUs.html" class="nav-link">About Us</a></li>
    <li><a href="https://github.com/AY2425S2-CS2103T-T14-2/tp" target="_blank" class="nav-link"><md>:fab-github:</md></a>
    </li>
    <li slot="right">
      <form class="navbar-form">
        <searchbar :data="searchData" placeholder="Search" :on-hit="searchCallback" menu-align-right></searchbar>
      </form>
    </li>
  </navbar>
</header>

<div id="flex-body">
  <nav id="site-nav">
    <div class="site-nav-top">
      <div class="fw-bold mb-2" style="font-size: 1.25rem;">Site Map</div>
    </div>
    <div class="nav-component slim-scroll">
      <site-nav>
* [Home]({{ baseUrl }}/index.html)
* [Features]({{ baseUrl }}/features.html) :expanded:
  * [Overview]({{ baseUrl }}/features.html#overview)
  * [Loan Management]({{ baseUrl }}/features.html#loan-management)
  * [Client Tracking]({{ baseUrl }}/features.html#client-tracking)
  * [Automated Reminders]({{ baseUrl }}/features.html#automated-reminders)
* [User Guide]({{ baseUrl }}/UserGuide.html) :expanded:
  * [Quick Start]({{ baseUrl }}/UserGuide.html#quick-start)
  * [Commands]({{ baseUrl }}/UserGuide.html#commands)
  * [FAQs]({{ baseUrl }}/UserGuide.html#faqs)
  * [Command Summary]({{ baseUrl }}/UserGuide.html#command-summary)
* [Developer Guide]({{ baseUrl }}/DeveloperGuide.html) :expanded:
  * [Setting Up]({{ baseUrl }}/DeveloperGuide.html#setting-up-getting-started)
  * [Implementation]({{ baseUrl }}/DeveloperGuide.html#implementation)
  * [Testing and Deployment]({{ baseUrl }}/DeveloperGuide.html#testing-and-deployment)
  * [Appendices]({{ baseUrl }}/DeveloperGuide.html#appendices)
* Tutorials
  * [Adding a Command]({{ baseUrl }}/tutorials/AddCommand.html)
  * [Modifying Loan Calculation]({{ baseUrl }}/tutorials/ModifyLoan.html)
* [About Us]({{ baseUrl }}/AboutUs.html)
      </site-nav>
    </div>
  </nav>

  <div id="content-wrapper">
    {{ content }}
  </div>

  <nav id="page-nav">
    <div class="nav-component slim-scroll">
      <page-nav />
    </div>
  </nav>

  <scroll-top-button></scroll-top-button>
</div>

<footer>
  <div class="text-center">
    <small>
      [<md>**Powered by**</md> <img src="https://markbind.org/favicon.ico" width="30"> {{MarkBind}}, generated on {{timestamp}}]
    </small>
  </div>
</footer>
