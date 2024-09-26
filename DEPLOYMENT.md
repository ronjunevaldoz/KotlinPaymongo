


## How do I deploy it to Maven Central?

The most part of the job is already automated for you. However, deployment to Maven Central requires some manual work from your side.

1. - [ ] Create an account at [Sonatype issue tracker](https://issues.sonatype.org/secure/Signup!default.jspa)
2. - [ ] [Create an issue](https://issues.sonatype.org/secure/CreateIssue.jspa?issuetype=21&pid=10134) to create new project for you
3. - [ ] You will have to prove that you own your desired namespace
4. - [ ] Create a GPG key with `gpg --gen-key`, use the same email address you used to sign up to the Sonatype Jira
5. - [ ] Find your key id in the output of the previous command looking like `D89FAAEB4CECAFD199A2F5E612C6F735F7A9A519`
6. - [ ] Upload your key to a keyserver, for example
 ```bash
 gpg --send-keys --keyserver keyserver.ubuntu.com "<your key id>"
 ```
1. - [ ] Now you should create secrets available to your GitHub Actions
    1. via `gh` command

 ```bash
 gh secret set SIGNING_KEY -a actions --body "$(gpg --export-secret-key --armor "<KEY_ID>")"
 gh secret set SIGNING_KEY_ID -a actions --body "<KEY_ID>"
 gh secret set SIGNING_KEY_PASSWORD -a actions --body "<SECRET_PASSWORD>"
 gh secret set MAVEN_CENTRAL_PASSWORD -a actions --body "<GENERATED_PASSWORD>"
 gh secret set MAVEN_CENTRAL_USERNAME -a actions --body "<GENERATED_USERNAME>"
 ```

1- [ ] Deploy command
`./gradlew publishAndReleaseToMavenCentral --no-configuration-cache`