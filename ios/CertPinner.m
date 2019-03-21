
#import "CertPinner.h"
#import <React/RCTLog.h>
#import "TrustKit/TrustKit.h"

@implementation CertPinner

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(enableCertPinning:(BOOL)enable)
{
  // Here's our method's code
  // [[TrustKit sharedInstance] pinningValidator].enforcePinning = enable;
  //RCTLogInfo(@"enableCertPinning called with %d!", enable);
}

@end
  
