## Available users:

1. **spr.only.admin.role.one.RH** - User with next organizations and roles:
    * Organization "Hachette Books, Inc." with roles "PUBPORTAL_SPR_ADMIN"

2. **spr.only.admin.role.several.RH** - User with next organizations and roles:
    * Organization "Hachette Books, Inc." with roles "PUBPORTAL_SPR_ADMIN"
    * Organization "Ref Pub B" with roles "PUBPORTAL_SPR_ADMIN"
    * Organization "Ref Pub C" with roles "PUBPORTAL_SPR_ADMIN"
    * Organization "Ref Pub D" with roles "PUBPORTAL_SPR_ADMIN"

3. **spr.admin.and.roa.role.one.RH** - User with admin role for SPR and additional roles for Author tab.
    * Organization "Hachette Books, Inc." with roles "PUBPORTAL_SPR_ADMIN"

4. **spr.admin.no.SPRs**
    * Organization "Ref Pub A" with roles "PUBPORTAL_SPR_ADMIN". No requests should be created for the organization.
5. **spr.only.scroll.in.Pub.ddl**
    *A lot of organizations for verifying ddl.

<a name="json-parametrization"/>

## JSON parametrization

Some identifiers and names could be filled into JSON automatically. In this case information about parametrization will
be used from properties file. To use this feature JSON should contain placeholders in the following format:
```[$placeholder_name:default_value]```.<br/>
Where ```placeholder_name``` should be the name from the property file. It could contain letters, digits or period(.).
For example ```placeholder.name.1``` is valid placeholder. Values which aren't able to be resolved won't be replaced by
anything.<br />
Use placeholder name in the format ```today +/- N days``` to set dynamic date in next format ```yyyy-MM-dd'T'HH:mm:ss'Z'```.<br />
The ```default_value``` is set if property is not found.<br />
In addition values for parametrization will be replaced by values from properties file if it is possible.

To set parameters from steps use following format:
```{$placeholder_name:default_value}```.<br/>

<a name="org-parametrization"/>

## Organization parametrization

Property file contains properties for Telesales org id, org uid, org name.
Example:
pubportal.spr.ui.organizations.main.test.org.telesales.id=7001360254
pubportal.spr.ui.organizations.main.test.org.uid=ac729b8b-f84e-466a-8fff-2e30a1ba69c8

<a name="examples-tables-issues"/>

## Examples tables issues

Values which a passed using examples tables are not supporting escaping of characters. So it's not possible to use
such characters as ```\n```, ```\\``` etc there.
To resolve this issue this values could be processed inside steps implementation using
```ParametersUtils.unescapeParameter(text)``` call. <br>
Another possible issue is using ```$``` as a part of value from examples table. Unfortunately there is no solution for
it. So it would be great to avoid such characters or pass it without examples tables.

<a name="creation-of-publication-names"/>

## Creation of publication names

Our current mechanism for creation of predefined publication names
creates journals with following parameters:

| Journal code  | Journal name | Journal group |
| ------------- | ------------- | ------------- |
| ```TA_ONESPACE_IN_JOURNAL_NAME```  | ```Test Automation one space in journal name abc def ghi jkl mno pqrs tuv wxyz ABC DEF GHI JKL MNO PQRS TUV WXYZ !"§ %& /() =?* '<> #; ²³~ @`´ ©«» ¤¼× {} abc.```  | ```ACUPMED``` |
| ```TA_SEVERAL_SPACES_IN_JOURNAL_NAME```  | ```Test Automation  several  spaces  in  journal  name  abc  def  ghi  jkl  mno  pqrs  tuv  wxyz  ABC  DEF  GHI  JKL  MNO  PQRS  TUV  WXYZ  !"§  %&  /()  =?* '<>  #;  ²³~  @`´  ©«»  ¤¼×  {} .```  | ```ACUPMED``` |
| ```TA_NINJA_SQUAD```  | ```Ninja Squad```  | ```ACUPMED``` |
