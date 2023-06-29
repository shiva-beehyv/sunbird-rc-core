// Code generated by go-swagger; DO NOT EDIT.

package notification

// This file was generated by the swagger tool.
// Editing this file might prove futile when you re-run the generate command

import (
	"net/http"

	"github.com/go-openapi/runtime/middleware"
)

// GetNotificationHandlerFunc turns a function with the right signature into a get notification handler
type GetNotificationHandlerFunc func(GetNotificationParams) middleware.Responder

// Handle executing the request and returning a response
func (fn GetNotificationHandlerFunc) Handle(params GetNotificationParams) middleware.Responder {
	return fn(params)
}

// GetNotificationHandler interface for that can handle valid get notification params
type GetNotificationHandler interface {
	Handle(GetNotificationParams) middleware.Responder
}

// NewGetNotification creates a new http.Handler for the get notification operation
func NewGetNotification(ctx *middleware.Context, handler GetNotificationHandler) *GetNotification {
	return &GetNotification{Context: ctx, Handler: handler}
}

/*
	GetNotification swagger:route GET /notification notification getNotification

# Get the last notifications sent

Temporary API to get the last notifications sent
*/
type GetNotification struct {
	Context *middleware.Context
	Handler GetNotificationHandler
}

func (o *GetNotification) ServeHTTP(rw http.ResponseWriter, r *http.Request) {
	route, rCtx, _ := o.Context.RouteInfo(r)
	if rCtx != nil {
		*r = *rCtx
	}
	var Params = NewGetNotificationParams()
	if err := o.Context.BindValidRequest(r, route, &Params); err != nil { // bind params
		o.Context.Respond(rw, r, route.Produces, route, err)
		return
	}

	res := o.Handler.Handle(Params) // actually handle the request
	o.Context.Respond(rw, r, route.Produces, route, res)

}
